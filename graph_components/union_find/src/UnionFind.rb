# Ruby
# グラフの連結成分: Union-Find

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

  def get_vertice(vertex)
    # 頂点がグラフに存在するか確認する
    if @data.key?(vertex)
      # 存在する場合は、その頂点の隣接リスト（関連する値）を返す
      @data[vertex]
    else
      # 存在しない場合はメッセージを表示し、nilを返す
      puts "ERROR: #{vertex}は範囲外です"
      nil
    end
  end

  def get_edge(vertex1, vertex2)
    # 指定された2つの頂点間に辺が存在するかを確認する
    # 両方の頂点がグラフに存在する必要がある
    if @data.key?(vertex1) && @data.key?(vertex2)
      # vertex1の隣接リストにvertex2が含まれているかを確認
      # 無向グラフなので、片方を確認すれば十分
      @data[vertex1].any? { |neighbor, _| neighbor == vertex2 }
    else
      # どちらかの頂点が存在しない場合は辺も存在しない
      false
    end
  end

  def add_vertex(vertex)
    # 新しい頂点をグラフに追加します。
    unless @data.key?(vertex)
      @data[vertex] = []
    end
    true # 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
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
  
  def remove_vertex(vertex)
    # 頂点とそれに関連する辺を削除します。
    if @data.key?(vertex)
      # この頂点への参照を他の頂点の隣接リストから削除する
      @data.each do |v, neighbors|
        @data[v] = neighbors.reject { |neighbor, _| neighbor == vertex }
      end
      # 頂点自体を削除する
      @data.delete(vertex)
      true
    else
      puts "ERROR: #{vertex} は範囲外です"
      false
    end
  end

  def remove_edge(vertex1, vertex2)
    # 両頂点間の辺を削除します。
    if @data.key?(vertex1) && @data.key?(vertex2)
      removed = false
      # vertex1 から vertex2 への辺を削除
      original_len_v1 = @data[vertex1].length
      @data[vertex1] = @data[vertex1].reject { |neighbor, _| neighbor == vertex2 }
      removed = true if @data[vertex1].length < original_len_v1

      # vertex2 から vertex1 への辺を削除
      original_len_v2 = @data[vertex2].length
      @data[vertex2] = @data[vertex2].reject { |neighbor, _| neighbor == vertex1 }
      removed = true if @data[vertex2].length < original_len_v2
      
      removed # 少なくとも片方向が削除されたか
    else
      puts "ERROR: #{vertex1} または #{vertex2} は範囲外です"
      false
    end
  end

  def is_empty?
    # グラフが空かどうか
    @data.empty?
  end
  
  def size
    # グラフの頂点数を返す
    @data.size
  end
  
  def clear
    # グラフを空にする
    @data = {}
    true
  end

  def get_connected_components
    return [] if @data.empty? # 空のグラフの場合は空配列を返す

    # Union-Findのためのデータ構造を初期化
    # parent[i] は要素 i の親を示す
    # size[i] は要素 i を根とする集合のサイズを示す (Union by Size用)
    parent = {}
    size = {}

    # 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
    vertices = get_vertices
    vertices.each do |vertex|
      parent[vertex] = vertex
      size[vertex] = 1
    end

    # 経路圧縮 (Path Compression) を伴う Find 操作
    find = lambda do |v|
      # vの親がv自身でなければ、根を探しにいく
      if parent[v] != v
        # 見つけた根をvの直接の親として記録 (経路圧縮)
        parent[v] = find.call(parent[v])
      end
      parent[v] # 最終的に根を返す
    end

    # Union by Size を伴う Union 操作
    union = lambda do |u, v|
      root_u = find.call(u)
      root_v = find.call(v)

      # 根が同じ場合は、すでに同じ集合に属しているので何もしない
      if root_u != root_v
        # より小さいサイズの木を大きいサイズの木に結合する
        if size[root_u] < size[root_v]
          parent[root_u] = root_v
          size[root_v] += size[root_u]
        else
          parent[root_v] = root_u
          size[root_u] += size[root_v]
        end
        true # 結合が行われた
      else
        false # 結合は行われなかった
      end
    end

    # グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
    get_edges.each do |u, v, _|
      union.call(u, v)
    end

    # 連結成分をグループ化する
    # 根をキーとして、その根に属する頂点のリストを値とするハッシュを作成
    components = {}
    vertices.each do |vertex|
      root = find.call(vertex) # 各頂点の最終的な根を見つける
      components[root] ||= []
      components[root] << vertex
    end

    # 連結成分のリスト（値の部分）を返す
    components.values
  end
end

def main
  puts "UnionFind TEST -----> start"

  puts "\nnew"
  graph_data = GraphData.new
  puts "  現在のデータ: #{graph_data.get}"

  puts "\nadd_edge"
  graph_data.clear
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]]
  input_list.each do |input|
    puts "  入力値: #{input}"
    output = graph_data.add_edge(*input)
    puts "  出力値: #{output}"
  end
  puts "  現在のデータ: #{graph_data.get}"
  puts "\nget_connected_components"
  output = graph_data.get_connected_components
  puts "  連結成分: #{output}"

  puts "\nadd_edge"
  graph_data.clear
  input_list = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]]
  input_list.each do |input|
    puts "  入力値: #{input}"
    output = graph_data.add_edge(*input)
    puts "  出力値: #{output}"
  end
  puts "  現在のデータ: #{graph_data.get}"
  puts "\nget_connected_components"
  output = graph_data.get_connected_components
  puts "  連結成分: #{output}"

  puts "\nadd_edge"
  graph_data.clear        
  input_list = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]]
  input_list.each do |input|
    puts "  入力値: #{input}"
    output = graph_data.add_edge(*input)
    puts "  出力値: #{output}"
  end
  puts "  現在のデータ: #{graph_data.get}"
  puts "\nget_connected_components"
  output = graph_data.get_connected_components
  puts "  連結成分: #{output}"

  puts "\nadd_edge"
  graph_data.clear
  input_list = []
  input_list.each do |input|
    puts "  入力値: #{input}"
    output = graph_data.add_edge(*input)
    puts "  出力値: #{output}"
  end
  puts "  現在のデータ: #{graph_data.get}"
  puts "\nget_connected_components"
  output = graph_data.get_connected_components
  puts "  連結成分: #{output}"

  puts "\nUnionFind TEST <----- end"
end

if __FILE__ == $0
  require 'set'
  main
end