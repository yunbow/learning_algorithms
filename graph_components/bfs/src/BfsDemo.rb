# Ruby
# グラフの連結成分: BFS

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
    if @data.has_key?(vertex)
      @data[vertex]
    else
      nil # 頂点が存在しない場合はnilを返す
    end
  end

  def get_edge_weight(vertex1, vertex2)
    # 指定された2つの頂点間の辺の重みを返します。
    # 辺が存在しない場合はnilを返します。
    if @data.has_key?(vertex1) && @data.has_key?(vertex2)
      @data[vertex1].each do |neighbor, weight|
        return weight if neighbor == vertex2
      end
    end
    nil # 辺が存在しない場合
  end

  def get_vertice(vertex)
    # 頂点がグラフに存在するか確認する
    if @data.has_key?(vertex)
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
    if @data.has_key?(vertex1) && @data.has_key?(vertex2)
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
    unless @data.has_key?(vertex)
      @data[vertex] = []
    end
    true # 既に存在する場合も変更なしでも成功とみなす
  end
  
  def add_edge(vertex1, vertex2, weight)
    # 両頂点間に辺を追加します。重みを指定します。
    # 頂点がグラフに存在しない場合は追加します。
    add_vertex(vertex1) unless @data.has_key?(vertex1)
    add_vertex(vertex2) unless @data.has_key?(vertex2)
    
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
    if @data.has_key?(vertex)
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
    if @data.has_key?(vertex1) && @data.has_key?(vertex2)
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
    """
    グラフの連結成分をBFSを使用して見つけます。

    Returns:
        Array: 連結成分のリスト。各要素は連結成分を構成する頂点のリストです。
    """
    visited = Set.new  # 全体の訪問済み頂点を記録するセット
    all_components = [] # 見つかった連結成分のリスト

    # グラフのすべての頂点を取得
    vertices = get_vertices

    # すべての頂点を順番にチェック
    vertices.each do |vertex|
      # もしその頂点がまだ訪問されていなければ、新しい連結成分の開始点
      unless visited.include?(vertex)
        current_component = [] # 現在探索中の連結成分
        queue = [vertex] # BFS用のキュー
        visited.add(vertex) # 開始点を訪問済みにマーク
        current_component << vertex # 開始点を現在の成分に追加

        # BFSを開始
        until queue.empty?
          u = queue.shift # キューから頂点を取り出す

          # 取り出した頂点の隣接リストを取得 (重み情報を含む)
          neighbors_with_weight = get_neighbors(u)

          # 頂点が存在し、隣接ノードがある場合
          if neighbors_with_weight
            # 隣接ノードだけを取り出してループ
            neighbors_with_weight.each do |neighbor, weight| # タプルを展開して隣接頂点を取得
              # 隣接する頂点がまだ訪問されていなければ
              unless visited.include?(neighbor) # 頂点そのものと比較
                visited.add(neighbor) # 頂点そのものを訪問済みにマーク
                queue << neighbor # 頂点そのものをキューに追加
                current_component << neighbor # 頂点そのものを現在の成分に追加
              end
            end
          end
        end

        # BFSが終了したら、1つの連結成分が見つかった
        all_components << current_component
      end
    end

    all_components
  end
end

def main
  puts "Bfs TEST -----> start"

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

  puts "Bfs TEST <----- end"
end

main if __FILE__ == $0