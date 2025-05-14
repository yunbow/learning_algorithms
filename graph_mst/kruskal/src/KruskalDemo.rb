# Ruby
# グラフの最小全域木: クラスカル法 (Kruskal)

class DSU
  def initialize(vertices)
    # 各頂点の親を格納します。最初は各頂点自身が親です。
    @parent = vertices.each_with_object({}) { |v, hash| hash[v] = v }
    # ランク（木の高さまたはサイズ）を格納し、union操作を最適化します。
    @rank = vertices.each_with_object({}) { |v, hash| hash[v] = 0 }
  end

  # 頂点 i が属する集合の代表元（根）を見つけます。
  # パス圧縮により効率化されます。
  def find(i)
    if @parent[i] == i
      return i
    end
    # パス圧縮: 探索中に encountered したノードを根に直接つなぎ直します。
    @parent[i] = find(@parent[i])
    return @parent[i]
  end

  # 頂点 i と 頂点 j を含む二つの集合を結合します。
  # ランクによるunionにより効率化されます。
  def union(i, j)
    root_i = find(i)
    root_j = find(j)

    # 根が異なる場合のみ結合します（同じ集合に属している場合は何もしません）。
    if root_i != root_j
      # ランクが小さい方の木を、ランクが大きい方の木の根に付けます。
      if @rank[root_i] < @rank[root_j]
        @parent[root_i] = root_j
      elsif @rank[root_i] > @rank[root_j]
        @parent[root_j] = root_i
      else
        # ランクが同じ場合はどちらかを親とし、その親のランクを増やします。
        @parent[root_j] = root_i
        @rank[root_i] += 1
      end
      return true # 集合が結合された
    end
    return false # 既に同じ集合に属していた
  end
end

# 重みを扱えるように改変された GraphData クラス
class GraphData
  def initialize
    # 隣接ノードとその辺の重みを格納します。
    # キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    # 例: { 'A' => [['B', 1], ['C', 4]], 'B' => [['A', 1], ['C', 2], ['D', 5]], ... }
    @data = {}
  end

  def get
    # グラフの内部データを取得します。
    return @data
  end

  def get_vertices
    # グラフの全頂点を配列として返します。
    return @data.keys
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
    return edges.to_a
  end

  def add_vertex(vertex)
    # 新しい頂点をグラフに追加します。
    if !@data.key?(vertex)
      @data[vertex] = []
      return true
    end
    # 既に存在する場合は追加しないがtrueを返す（変更なしでも成功とみなす）
    return true
  end

  def add_edge(vertex1, vertex2, weight)
    # 両頂点間に辺を追加します。重みを指定します。
    # 頂点がグラフに存在しない場合は追加します。
    if !@data.key?(vertex1)
      add_vertex(vertex1)
    end
    if !@data.key?(vertex2)
      add_vertex(vertex2)
    end
    
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
    if !edge_exists_v1v2
      @data[vertex1] << [vertex2, weight]
    end

    # vertex2 -> vertex1 の辺を追加（重み付き）
    edge_exists_v2v1 = false
    @data[vertex2].each_with_index do |(neighbor, _), i|
      if neighbor == vertex1
        @data[vertex2][i] = [vertex1, weight] # 既に存在する場合は重みを更新
        edge_exists_v2v1 = true
        break
      end
    end
    if !edge_exists_v2v1
      @data[vertex2] << [vertex1, weight]
    end
    
    return true
  end

  def clear
    # グラフを空にします。
    @data = {}
    return true
  end

  def get_mst
    # 1. 全ての辺を取得し、重みでソートします。
    # get_edges() は [u, v, weight] の配列を返します。
    edges = get_edges
    # 重み (配列の3番目の要素) をキーとして辺をソート
    sorted_edges = edges.sort_by { |edge| edge[2] }

    # 2. Union-Findデータ構造を初期化します。
    # 各頂点が自身の集合に属するようにします。
    vertices = get_vertices
    dsu = DSU.new(vertices)

    # 3. MSTを構築します。
    # 結果として得られるMSTの辺を格納する配列
    mst_edges = []
    # MSTに追加された辺の数 (頂点数-1 になればMSTが完成)
    edges_count = 0

    # ソートされた辺を順番に調べます。
    sorted_edges.each do |u, v, weight|
      # 辺 (u, v) の両端点が属する集合の代表元（根）を見つけます。
      root_u = dsu.find(u)
      root_v = dsu.find(v)

      # 両端点が異なる集合に属する場合、その辺をMSTに追加してもサイクルは形成されません。
      if root_u != root_v
        # 辺をMSTに追加します。
        mst_edges << [u, v, weight]
        # 辺を追加したので、両端点の集合を結合します。
        dsu.union(u, v)
        # MSTに追加した辺の数を増やします。
        edges_count += 1

        # 頂点数から1を引いた数の辺がMSTに追加されたら終了です。
        # これはグラフが連結である場合に限り、完全なMSTが得られたことを意味します。
        # 連結でない場合は、全ての辺を調べ終えるまで続行し、最小全域森を得ます。
        if edges_count == vertices.length - 1
          break
        end
      end
    end

    # MST (または最小全域森) の辺の配列を返します。
    return mst_edges
  end
end

require 'set'

def main
  puts "Kruskal TEST -----> start"
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

  puts "\nKruskal TEST <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end