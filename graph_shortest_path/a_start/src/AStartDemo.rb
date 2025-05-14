# Ruby
# グラフの最短経路: A-star

require 'set'

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
    # グラフの全頂点を配列として返します。
    @data.keys
  end

  def get_edges
    # グラフの全辺を配列として返します。
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
    # 指定された頂点の隣接ノードと辺の重みの配列を返します。
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
      puts "ERROR: 開始頂点または終了頂点がグラフに存在しません。"
      return nil, Float::INFINITY
    end

    return [start_vertex], 0 if start_vertex == end_vertex

    # g_costs: 開始ノードから各ノードまでの既知の最短コスト
    g_costs = {}
    @data.each_key { |vertex| g_costs[vertex] = Float::INFINITY }
    g_costs[start_vertex] = 0

    # f_costs: g_costs + ヒューリスティックコスト (推定合計コスト)
    f_costs = {}
    @data.each_key { |vertex| f_costs[vertex] = Float::INFINITY }
    f_costs[start_vertex] = heuristic.call(start_vertex, end_vertex)

    # came_from: 最短経路で各ノードの直前のノードを記録
    came_from = {}

    # open_set: 探索すべきノードの優先度キュー (f_cost, node)
    # Rubyの優先度キューの代替実装
    open_set = []
    open_set << [f_costs[start_vertex], start_vertex]

    while !open_set.empty?
      # open_set から最も f_cost が低いノードを取り出す
      open_set.sort! { |a, b| a[0] <=> b[0] }
      current_f_cost, current_vertex = open_set.shift

      # 取り出したノードの f_cost が、記録されている f_costs[current_vertex] より大きい場合、
      # それは古い情報なので無視して次のノードに進む
      next if current_f_cost > f_costs[current_vertex]

      # 目標ノードに到達した場合、経路を再構築して返す
      if current_vertex == end_vertex
        return reconstruct_path(came_from, end_vertex), g_costs[end_vertex]
      end

      # 現在のノードの隣接ノードを調べる
      neighbors = get_neighbors(current_vertex)
      next if neighbors.nil? # 孤立したノードの場合など

      neighbors.each do |neighbor, weight|
        # 現在のノードを経由した場合の隣接ノードへの新しい g_cost
        tentative_g_cost = g_costs[current_vertex] + weight

        # 新しい g_cost が、現在記録されている隣接ノードへの g_cost よりも小さい場合
        if tentative_g_cost < g_costs[neighbor]
          # 経路情報を更新
          came_from[neighbor] = current_vertex
          g_costs[neighbor] = tentative_g_cost
          f_costs[neighbor] = g_costs[neighbor] + heuristic.call(neighbor, end_vertex)
          
          # 隣接ノードを open_set に追加（または優先度を更新）
          open_set << [f_costs[neighbor], neighbor]
        end
      end
    end

    # open_set が空になっても目標ノードに到達しなかった場合、経路は存在しない
    return nil, Float::INFINITY
  end

  private

  def reconstruct_path(came_from, current_vertex)
    path = []
    while came_from.key?(current_vertex)
      path << current_vertex
      current_vertex = came_from[current_vertex]
    end
    path << current_vertex # 開始ノードを追加
    path.reverse # 経路を逆順にする (開始 -> 目標)
  end
end

# ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
# 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
dummy_heuristic = lambda do |u, v|
  # u と v の間に何らかの推定距離を計算する関数
  # ここではダミーとして常に0を返す
  0
end

def main
  puts "A-start TEST -----> start"

  graph_data = GraphData.new

  graph_data.clear
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'B']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  graph_data.clear
  input_list = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'B']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  graph_data.clear
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'D']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  graph_data.clear
  input_list = []
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  input = ['A', 'B']
  shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
  puts "経路#{input[0]}-#{input[1]} の最短経路は #{shortest_path[0]} (重み: #{shortest_path[1]})"

  puts "\nA-start TEST <----- end"
end

if __FILE__ == $0
  main
end