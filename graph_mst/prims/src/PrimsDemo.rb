# Ruby
# グラフの最小全域木: プリム法 (Prim)

class GraphData
  def initialize
    # 隣接リストとしてグラフデータを格納します。
    # キーは頂点、値はその頂点に隣接する頂点とその辺の重みのタプルのリストです。
    # 例: { 'A' => [['B', 1], ['C', 4]], 'B' => [['A', 1], ['C', 2]], ... }
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

  def get_edge_weight(vertex1, vertex2)
    # 指定された2つの頂点間の辺の重みを返します。
    # 辺が存在しない場合はnilを返します。
    if @data.key?(vertex1) && @data.key?(vertex2)
      @data[vertex1].each do |neighbor, weight|
        return weight if neighbor == vertex2
      end
    end
    nil # 辺が存在しない場合
  end

  def add_vertex(vertex)
    # 新しい頂点をグラフに追加します。
    unless @data.key?(vertex)
      @data[vertex] = []
    end
    true # 既に存在する場合も成功とみなす
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
  
  def is_empty?
    # グラフが空かどうかを返します。
    @data.empty?
  end
    
  def clear
    # グラフを空にします。
    @data = {}
    true
  end

  def get_mst(start_vertex = nil)
    vertices = get_vertices
    return [] if vertices.empty? # グラフが空

    start_vertex = vertices.first if start_vertex.nil?
    unless @data.key?(start_vertex)
      puts "ERROR: 開始頂点 #{start_vertex} はグラフに存在しません。"
      return nil
    end

    # MSTに含まれる頂点のセット
    in_mst = Set.new
    # 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
    # Rubyには優先度付きキューのライブラリがないため、配列とソートで代用
    min_heap = []
    # MSTを構成する辺のリスト
    mst_edges = []
    # 各頂点への最小コスト（MSTに追加する際の辺の重み）と、その遷移元の頂点を記録
    min_cost = {}
    parent = {}
    
    vertices.each do |v|
      min_cost[v] = Float::INFINITY
      parent[v] = nil
    end

    # 開始頂点の処理
    min_cost[start_vertex] = 0
    min_heap << [0, start_vertex, nil] # [コスト, 現在の頂点, 遷移元の頂点]

    until min_heap.empty?
      # 配列をコストでソートして最小値を取り出す
      min_heap.sort_by! { |cost, _, _| cost }
      cost, current_vertex, from_vertex = min_heap.shift

      # 既にMSTに含まれている頂点であればスキップ
      next if in_mst.include?(current_vertex)

      # 現在の頂点をMSTに追加
      in_mst.add(current_vertex)

      # MSTに追加された辺を記録 (開始頂点以外)
      if from_vertex
        # from_vertex から current_vertex への辺の重みを取得
        weight = get_edge_weight(from_vertex, current_vertex)
        if weight
          # 辺を正規化して追加
          mst_edges << [from_vertex, current_vertex].sort + [weight]
        end
      end

      # 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
      neighbors_with_weight = get_neighbors(current_vertex)
      if neighbors_with_weight # 隣接する頂点がある場合
        neighbors_with_weight.each do |neighbor, weight|
          # 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
          if !in_mst.include?(neighbor) && weight < min_cost[neighbor]
            min_cost[neighbor] = weight
            parent[neighbor] = current_vertex
            min_heap << [weight, neighbor, current_vertex]
          end
        end
      end
    end

    mst_edges
  end
end

def main
  puts "Prims TEST -----> start"
  require 'set'
  graph_data = GraphData.new

  graph_data.clear
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  output_mst = graph_data.get_mst
  output_mst.each do |edge|
    puts "Edge: #{edge[0]} - #{edge[1]}, Weight: #{edge[2]}"
  end
  total_weight = output_mst.sum { |edge| edge[2] }
  puts "最小全域木の合計重み: #{total_weight}"

  graph_data.clear
  input_list = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  output_mst = graph_data.get_mst
  output_mst.each do |edge|
    puts "Edge: #{edge[0]} - #{edge[1]}, Weight: #{edge[2]}"
  end
  total_weight = output_mst.sum { |edge| edge[2] }
  puts "最小全域木の合計重み: #{total_weight}"

  graph_data.clear
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]]
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  output_mst = graph_data.get_mst
  output_mst.each do |edge|
    puts "Edge: #{edge[0]} - #{edge[1]}, Weight: #{edge[2]}"
  end
  total_weight = output_mst.sum { |edge| edge[2] }
  puts "最小全域木の合計重み: #{total_weight}"

  graph_data.clear
  input_list = []
  input_list.each do |input|
    graph_data.add_edge(*input)
  end
  puts "\nグラフの頂点: #{graph_data.get_vertices}"
  puts "グラフの辺 (重み付き): #{graph_data.get_edges}"
  output_mst = graph_data.get_mst
  output_mst.each do |edge|
    puts "Edge: #{edge[0]} - #{edge[1]}, Weight: #{edge[2]}"
  end
  total_weight = output_mst.sum { |edge| edge[2] }
  puts "最小全域木の合計重み: #{total_weight}"

  puts "\nPrims TEST <----- end"
end

if __FILE__ == $0
  main
end