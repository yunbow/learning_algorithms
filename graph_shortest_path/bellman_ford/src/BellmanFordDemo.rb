# Ruby
# グラフの最短経路: ベルマンフォード法 (Bellman Ford)

class GraphData
  def initialize
    # 隣接ノードとその辺の重みを格納します。
    # キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
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
    # ベルマン-フォード法で使用するため、内部データから有向辺として抽出します。
    # 各辺は [出発頂点, 到着頂点, 重み] の配列になります。
    edges = []
    @data.each do |u, neighbors|
      neighbors.each do |v, weight|
        edges << [u, v, weight]
      end
    end
    edges
  end

  def add_vertex(vertex)
    # 新しい頂点をグラフに追加します。
    @data[vertex] = [] unless @data.key?(vertex)
    true # 既に存在する場合は追加しないがtrueを返す
  end

  def add_edge(vertex1, vertex2, weight)
    # 両頂点間に辺を追加します。重みを指定します。
    # 頂点がグラフに存在しない場合は追加します。
    add_vertex(vertex1)
    add_vertex(vertex2)

    # vertex1 -> vertex2 の辺を追加（重み付き）
    # 既に同じ頂点間の辺が存在する場合は重みを更新
    edge_updated_v1v2 = false
    @data[vertex1].each_with_index do |(neighbor, _), i|
      if neighbor == vertex2
        @data[vertex1][i] = [vertex2, weight]
        edge_updated_v1v2 = true
        break
      end
    end
    @data[vertex1] << [vertex2, weight] unless edge_updated_v1v2

    # vertex2 -> vertex1 の辺を追加（重み付き）
    # 既に同じ頂点間の辺が存在する場合は重みを更新
    edge_updated_v2v1 = false
    @data[vertex2].each_with_index do |(neighbor, _), i|
      if neighbor == vertex1
        @data[vertex2][i] = [vertex1, weight]
        edge_updated_v2v1 = true
        break
      end
    end
    @data[vertex2] << [vertex1, weight] unless edge_updated_v2v1

    true
  end

  def is_empty?
    # グラフが空かどうかを返します。
    @data.empty?
  end

  def clear
    # グラフを空にします。
    @data = {}
    true
  end

  def get_shortest_path(start_vertex, end_vertex, heuristic)
    vertices = get_vertices
    edges = get_edges # 有向辺のリストを取得
    num_vertices = vertices.length

    # 始点と終点の存在チェック
    unless vertices.include?(start_vertex)
      puts "エラー: 始点 '#{start_vertex}' がグラフに存在しません。"
      return [[], Float::INFINITY]
    end
    unless vertices.include?(end_vertex)
      puts "エラー: 終点 '#{end_vertex}' がグラフに存在しません。"
      return [[], Float::INFINITY]
    end

    # 始点と終点が同じ場合
    if start_vertex == end_vertex
      return [[start_vertex], 0]
    end

    # 距離と先行頂点の初期化
    # dist: 始点からの最短距離を格納
    # pred: 最短経路における各頂点の先行頂点を格納
    dist = {}
    pred = {}
    vertices.each do |vertex|
      dist[vertex] = Float::INFINITY
      pred[vertex] = nil
    end
    dist[start_vertex] = 0 # 始点自身の距離は0

    # |V| - 1 回の緩和ステップを実行
    # このループの後、負閉路が存在しない場合は全ての頂点への最短距離が確定している
    (num_vertices - 1).times do
      # 緩和が一度も行われなかった場合にループを中断するためのフラグ
      relaxed_in_this_iteration = false
      edges.each do |u, v, weight|
        # dist[u] が無限大でない場合のみ緩和を試みる（到達不可能な頂点からの緩和は意味がない）
        if dist[u] != Float::INFINITY && dist[u] + weight < dist[v]
          dist[v] = dist[u] + weight
          pred[v] = u
          relaxed_in_this_iteration = true
        end
      end
      # このイテレーションで緩和が行われなかった場合は、それ以上距離が更新されることはないのでループを抜ける
      break unless relaxed_in_this_iteration
    end

    # 負閉路の検出
    # もう一度全ての辺に対して緩和を試みる。
    # ここでさらに距離が更新される辺が存在する場合、その辺は負閉路の一部であるか、
    # 負閉路から到達可能な頂点への辺である。
    edges.each do |u, v, weight|
      if dist[u] != Float::INFINITY && dist[u] + weight < dist[v]
        # 負閉路が存在します。
        puts "エラー: グラフに負閉路が存在します。最短経路は定義できません。"
        return [nil, -Float::INFINITY] # 負の無限大を返すことで、距離が無限小になることを示す
      end
    end

    # 最短経路の構築
    path = []
    current = end_vertex

    # 終点まで到達不可能かチェック (距離が初期値のままか)
    if dist[end_vertex] == Float::INFINITY
      return [[], Float::INFINITY] # 到達不可能
    end

    # 終点から先行頂点をたどって経路を逆順に構築
    while !current.nil?
      path << current
      # 始点に到達したらループを終了
      break if current == start_vertex
      # 次の頂点に進む
      current = pred[current]
    end

    # 経路が始点から始まっていない場合 (通常は到達不可能な場合に含まれるはずだが、念のため)
    # pathリストの最後の要素がstart_vertexであることを確認
    if path.empty? || path.last != start_vertex
      # このケースは dist[end_vertex] == Float::INFINITY で既に処理されているはずだが、念のため
      return [[], Float::INFINITY]
    end

    path.reverse! # 経路を始点から終点の順にする

    [path, dist[end_vertex]]
  end
end

# ヒューリスティック関数 (ベルマン-フォード法では使用しないが、元のコードに合わせた引数のために残す)
def dummy_heuristic(u, v)
  # u と v の間に何らかの推定距離を計算する関数
  # ここではベルマン-フォード法では使用しないため、常に0を返す
  0
end

def main
  puts "BellmanFord TEST -----> start"

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

  puts "\nBellmanFord TEST <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end