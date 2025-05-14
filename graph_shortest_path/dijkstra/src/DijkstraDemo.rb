# Ruby
# グラフの最短経路: ダイクストラ法 (dijkstra)

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
    if @data.key?(vertex)
      @data[vertex]
    else
      nil # 頂点が存在しない場合はnilを返す
    end
  end

  def add_vertex(vertex)
    # 新しい頂点をグラフに追加します。
    unless @data.key?(vertex)
      @data[vertex] = []
      return true
    end
    # 既に存在する場合は追加しないがtrueを返す（変更なしでも成功とみなす）
    true
  end

  def add_edge(vertex1, vertex2, weight)
    # 両頂点間に辺を追加します。重みを指定します。
    # 頂点がグラフに存在しない場合は追加します。
    add_vertex(vertex1) unless @data.key?(vertex1)
    add_vertex(vertex2) unless @data.key?(vertex2)
    
    # 両方向に辺を追加する（無向グラフ）
    # 既に同じ辺が存在する場合は重みを更新するかどうか？
    # 今回は単純に追加する（重複辺はプリム法では問題にならないが、データとしては綺麗でないかも）
    # ここでは、同じ頂点間の辺が存在しない場合のみ追加するように修正します。
    
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
    unless @data.key?(start_vertex) && @data.key?(end_vertex)
      puts "ERROR: 開始頂点 '#{start_vertex}' または 終了頂点 '#{end_vertex}' がグラフに存在しません。"
      return [nil, Float::INFINITY]
    end

    # 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
    distances = {}
    @data.each_key { |vertex| distances[vertex] = Float::INFINITY }
    distances[start_vertex] = 0

    # 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
    predecessors = {}
    @data.each_key { |vertex| predecessors[vertex] = nil }

    # 優先度付きキュー: [距離, 頂点] の配列を格納し、距離が小さい順に取り出す
    # Rubyの場合、PriorityQueueが標準ライブラリにはないため、配列を使って実装します
    priority_queue = [[0, start_vertex]] # [開始頂点への距離, 開始頂点]

    until priority_queue.empty?
      # 優先度付きキューから最も距離の小さい頂点を取り出す
      priority_queue.sort! # 距離順にソート
      current_distance, current_vertex = priority_queue.shift # 最小距離の要素を取り出す

      # 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
      # より短い経路が既に見つかっているためスキップ
      next if current_distance > distances[current_vertex]

      # 終了頂点に到達したら探索終了
      break if current_vertex == end_vertex # 最短経路が見つかった

      # 現在の頂点から到達可能な隣接頂点を探索
      # self.get_neighbors(current_vertex) は [[neighbor, weight], ...] のリストを返す
      get_neighbors(current_vertex).each do |neighbor, weight|
        distance_through_current = distances[current_vertex] + weight

        # より短い経路が見つかった場合
        if distance_through_current < distances[neighbor]
          distances[neighbor] = distance_through_current
          predecessors[neighbor] = current_vertex
          # 優先度付きキューに隣接頂点を追加または更新
          # ダイクストラ法では heuristic は使用しない (または h=0)
          priority_queue.push([distance_through_current, neighbor])
        end
      end
    end

    # 終了頂点への最短距離が無限大のままなら、到達不可能
    if distances[end_vertex] == Float::INFINITY
      puts "INFO: 開始頂点 '#{start_vertex}' から 終了頂点 '#{end_vertex}' への経路は存在しません。"
      return [nil, Float::INFINITY]
    end

    # 最短経路を再構築
    path = []
    current = end_vertex
    while current
      path.unshift(current) # 経路の先頭に追加（逆順を避けるため）
      current = predecessors[current]
    end

    # 開始ノードから開始されていることを確認
    if path[0] != start_vertex
      # これが発生するのは、開始頂点が存在しないか、到達不能な場合だが、
      # 前段のチェックで大部分はカバーされているはず。
      # ここに来る場合は、特殊なケース（例えば孤立した開始点と終了点）が考えられる。
      return [nil, Float::INFINITY]
    end

    [path, distances[end_vertex]]
  end
end

# ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
# 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
def dummy_heuristic(u, v)
  # u と v の間に何らかの推定距離を計算する関数
  # ここではダミーとして常に0を返す
  0
end

def main
  puts "Dijkstra -----> start"

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

  puts "\nDijkstra <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end