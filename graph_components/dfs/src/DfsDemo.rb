# Ruby
# グラフの連結成分: DFS

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
    # グラフを空にする
    @data = {}
    true
  end

  def _dfs(vertex, visited, current_component)
    # 現在の頂点を訪問済みにマークし、現在の成分に追加
    visited.add(vertex)
    current_component << vertex

    # 隣接する頂点を探索
    # @data[vertex] || [] は、vertexが存在しない場合でも
    # エラーにならず空配列を返す安全な方法
    # ここで取得されるのは [[neighbor, weight], ...] の配列です。
    (@data[vertex] || []).each do |neighbor_info|
      # neighbor_info は [neighbor, weight] の配列なので、
      # 配列の0番目の要素（隣接頂点そのもの）を取得します。
      neighbor_vertex = neighbor_info[0]

      # まだ訪問していない隣接頂点に対して再帰的にDFSを呼び出す
      unless visited.include?(neighbor_vertex)
        _dfs(neighbor_vertex, visited, current_component)
      end
    end
  end

  def get_connected_components
    visited = Set.new # 訪問済み頂点を記録するセット
    connected_components = [] # 連結成分を格納する配列

    # グラフの全頂点を順にチェック
    get_vertices.each do |vertex|
      # まだ訪問していない頂点からDFSを開始
      unless visited.include?(vertex)
        current_component = [] # 現在の連結成分を格納する配列
        # DFSヘルパー関数を呼び出し、現在の連結成分を探索
        _dfs(vertex, visited, current_component)
        # 探索で見つかった連結成分を結果配列に追加
        connected_components << current_component
      end
    end

    connected_components
  end
end

def main
  puts "Dfs TEST -----> start"

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

  puts "Dfs TEST <----- end"
end

# Rubyの慣習に従い、Setクラスを使用するためにrequireを追加
require 'set'

# Rubyのメインプログラム実行
if __FILE__ == $PROGRAM_NAME
  main
end