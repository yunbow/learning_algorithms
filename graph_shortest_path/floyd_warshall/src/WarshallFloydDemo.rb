# Ruby
# グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

class GraphData
  def initialize
    # 隣接ノードとその辺の重みを格納します。
    # キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    # 例: { 'A' => [['B', 1], ['C', 4]], 'B' => [['A', 1], ['C', 2], ['D', 5]], ... }
    @data = {}
  end

  def get
    # グラフの内部データを取得します。
    @data
  end

  def get_vertices
    # グラフの全頂点をリストとして返します。
    @data.keys
  end

  def get_edges
    # グラフの全辺をリストとして返します。
    # 無向グラフの場合、[u, v, weight] の形式で返します。
    # 重複を避けるためにセットを使用します。
    edges = Set.new
    @data.each do |vertex, neighbors|
      neighbors.each do |neighbor, weight|
        # 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
        edge = [vertex, neighbor].sort
        edges.add([edge[0], edge[1], weight]) # [u, v, weight] の形式で格納
      end
    end
    edges.to_a
  end

  def get_neighbors(vertex)
    # 指定された頂点の隣接ノードと辺の重みのリストを返します。
    # 形式: [[隣接頂点, 重み], ...]
    @data[vertex] if @data.key?(vertex)
  end

  def add_vertex(vertex)
    # 新しい頂点をグラフに追加します。
    if !@data.key?(vertex)
      @data[vertex] = []
      return true
    end
    # 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
    true
  end

  def add_edge(vertex1, vertex2, weight)
    # 両頂点間に辺を追加します。重みを指定します。
    # 頂点がグラフに存在しない場合は追加します。
    add_vertex(vertex1) unless @data.key?(vertex1) 
    add_vertex(vertex2) unless @data.key?(vertex2)
    
    # 両方向に辺を追加する（無向グラフ）
    # vertex1 -> vertex2 の辺を追加（重み付き）
    edge_exists_v1v2 = false
    @data[vertex1].each_with_index do |(neighbor, _), i|
      if neighbor == vertex2
        @data[vertex1][i] = [vertex2, weight] # 既に存在する場合は重みを更新
        edge_exists_v1v2 = true
        break
      end
    end
    @data[vertex1] << [vertex2, weight] unless edge_exists_v1v2

    # vertex2 -> vertex1 の辺を追加（重み付き）
    edge_exists_v2v1 = false
    @data[vertex2].each_with_index do |(neighbor, _), i|
      if neighbor == vertex1
        @data[vertex2][i] = [vertex1, weight] # 既に存在する場合は重みを更新
        edge_exists_v2v1 = true
        break
      end
    end
    @data[vertex2] << [vertex1, weight] unless edge_exists_v2v1
    
    true
  end

  def clear
    # グラフを空にします。
    @data = {}
    true
  end

  def get_shortest_path(start_vertex, end_vertex, heuristic)
    vertices = get_vertices
    num_vertices = vertices.size
    return [nil, Float::INFINITY] if num_vertices == 0

    # 頂点名をインデックスにマッピング
    vertex_to_index = {}
    index_to_vertex = {}
    vertices.each_with_index do |vertex, index|
      vertex_to_index[vertex] = index
      index_to_vertex[index] = vertex
    end

    # 開始・終了頂点が存在するか確認
    unless vertex_to_index.key?(start_vertex) && vertex_to_index.key?(end_vertex)
      puts "ERROR: #{start_vertex} または #{end_vertex} がグラフに存在しません。"
      return [nil, Float::INFINITY]
    end

    start_index = vertex_to_index[start_vertex]
    end_index = vertex_to_index[end_vertex]

    # 距離行列 (dist) と経路復元用行列 (next_node) を初期化
    inf = Float::INFINITY
    dist = Array.new(num_vertices) { Array.new(num_vertices, inf) }
    next_node = Array.new(num_vertices) { Array.new(num_vertices, nil) }

    # 初期距離と経路復元情報を設定
    num_vertices.times do |i|
      dist[i][i] = 0 # 自分自身への距離は0
      neighbors = get_neighbors(vertices[i]) || []
      neighbors.each do |neighbor, weight|
        j = vertex_to_index[neighbor]
        dist[i][j] = weight
        next_node[i][j] = j # iからjへの直接辺の場合、iの次はj
      end
    end

    # ワーシャル-フロイド法の本体
    # k: 中継点として使用する頂点のインデックス
    num_vertices.times do |k|
      # i: 開始頂点のインデックス
      num_vertices.times do |i|
        # j: 終了頂点のインデックス
        num_vertices.times do |j|
          # i -> k -> j の経路が i -> j の現在の経路より短い場合
          if dist[i][k] != inf && dist[k][j] != inf && dist[i][k] + dist[k][j] < dist[i][j]
            dist[i][j] = dist[i][k] + dist[k][j]
            next_node[i][j] = next_node[i][k] # iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点
          end
        end
      end
    end

    # 指定された開始・終了頂点間の最短経路と重みを取得
    shortest_distance = dist[start_index][end_index]

    # 経路が存在しない場合 (距離がINF)
    return [nil, inf] if shortest_distance == inf

    # 経路を復元
    path = []
    u = start_index
    # 開始と終了が同じ場合は経路は開始頂点のみ
    if u == end_index
      path = [start_vertex]
    else
      # next_nodeを使って経路をたどる
      while u && u != end_index
        path << index_to_vertex[u]
        u = next_node[u][end_index]
        # 無限ループ防止のための簡易チェック (到達不能なのにnext_nodeがNoneでない場合など)
        if u && index_to_vertex[u] == path.last
          # 同じ頂点に戻ってきたなど、異常な経路復元を防ぐ
          puts "WARNING: 経路復元中に異常を検出しました（#{index_to_vertex[u]}でループ？）。"
          path = nil
          shortest_distance = inf
          break
        end
      end
      # 最後のノード (end_vertex) を追加
      path << end_vertex if path
    end

    [path, shortest_distance]
  end
end

# ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
def dummy_heuristic(u, v)
  # u と v の間に何らかの推定距離を計算する関数
  # ここではダミーとして常に0を返す
  0
end

def main
  puts "WarshallFloyd -----> start"
  
  require 'set'
  graph_data = GraphData.new

  graph_data.clear
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'B']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], method(:dummy_heuristic))
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  graph_data.clear
  input_list = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'B']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], method(:dummy_heuristic))
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  graph_data.clear
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'D']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], method(:dummy_heuristic))
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  graph_data.clear
  input_list = []
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'B']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], method(:dummy_heuristic))
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  puts "\nWarshallFloyd <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end