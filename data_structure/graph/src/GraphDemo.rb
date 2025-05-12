# Ruby
# データ構造: グラフ (Graph)

class GraphData
  def initialize
    # Hash storing vertices and their adjacent vertices with weights
    # Format: { 'A' => [['B', 1], ['C', 4]], 'B' => [['A', 1], ['C', 2], ['D', 5]], ... }
    @data = {}
  end

  def get
    @data
  end

  def get_vertices
    @data.keys
  end
  
  def get_edges
    # Create a set of unique edges to avoid duplicates
    edges = Set.new
    @data.each do |vertex, neighbors|
      neighbors.each do |neighbor, weight|
        # Normalize the edge to prevent duplicates
        edge = [vertex, neighbor].sort
        edges.add([edge[0], edge[1], weight])
      end
    end
    edges.to_a
  end
  
  def get_neighbors(vertex)
    @data[vertex] || nil
  end

  def get_edge_weight(vertex1, vertex2)
    return nil unless @data.key?(vertex1) && @data.key?(vertex2)
    
    @data[vertex1].each do |neighbor, weight|
      return weight if neighbor == vertex2
    end
    nil
  end

  def get_vertice(vertex)
    if @data.key?(vertex)
      @data[vertex]
    else
      puts "ERROR: #{vertex} is out of range"
      nil
    end
  end

  def get_edge(vertex1, vertex2)
    return false unless @data.key?(vertex1) && @data.key?(vertex2)
    
    @data[vertex1].any? { |neighbor, _| neighbor == vertex2 }
  end

  def add_vertex(vertex)
    @data[vertex] = [] unless @data.key?(vertex)
    true
  end
  
  def add_edge(vertex1, vertex2, weight)
    add_vertex(vertex1) unless @data.key?(vertex1)
    add_vertex(vertex2) unless @data.key?(vertex2)
    
    # Add or update edge from vertex1 to vertex2
    edge_exists_v1v2 = @data[vertex1].any? do |neighbor, _|
      if neighbor == vertex2
        @data[vertex1][(@data[vertex1].index { |n, _ | n == vertex2 })] = [vertex2, weight]
        true
      end
    end
    @data[vertex1] << [vertex2, weight] unless edge_exists_v1v2

    # Add or update edge from vertex2 to vertex1
    edge_exists_v2v1 = @data[vertex2].any? do |neighbor, _|
      if neighbor == vertex1
        @data[vertex2][(@data[vertex2].index { |n, _ | n == vertex1 })] = [vertex1, weight]
        true
      end
    end
    @data[vertex2] << [vertex1, weight] unless edge_exists_v2v1
    
    true
  end
  
  def remove_vertex(vertex)
    if @data.key?(vertex)
      # Remove references to this vertex from other vertices
      @data.each_value do |neighbors|
        neighbors.reject! { |neighbor, _| neighbor == vertex }
      end
      # Remove the vertex itself
      @data.delete(vertex)
      true
    else
      puts "ERROR: #{vertex} is out of range"
      false
    end
  end

  def remove_edge(vertex1, vertex2)
    if @data.key?(vertex1) && @data.key?(vertex2)
      removed = false
      
      # Remove edge from vertex1 to vertex2
      original_len_v1 = @data[vertex1].length
      @data[vertex1].reject! { |neighbor, _| neighbor == vertex2 }
      removed = true if @data[vertex1].length < original_len_v1

      # Remove edge from vertex2 to vertex1
      original_len_v2 = @data[vertex2].length
      @data[vertex2].reject! { |neighbor, _| neighbor == vertex1 }
      removed = true if @data[vertex2].length < original_len_v2
      
      removed
    else
      puts "ERROR: #{vertex1} or #{vertex2} is out of range"
      false
    end
  end

  def is_empty?
    @data.empty?
  end
  
  def size
    @data.size
  end
  
  def clear
    @data.clear
    true
  end
end

def main
  puts "Graph TEST -----> start"

  puts "\nnew"
  graph_data = GraphData.new
  puts "  Current data: #{graph_data.get}"

  puts "\nis_empty?"
  output = graph_data.is_empty?
  puts "  Output value: #{output}"

  puts "\nsize"
  output = graph_data.size
  puts "  Output value: #{output}"

  input_list = ['A', 'B', 'C']
  input_list.each do |input|
    puts "\nadd_vertex"
    puts "  Input value: #{input}"
    output = graph_data.add_vertex(input)
    puts "  Output value: #{output}"
  end
  puts "  Current data: #{graph_data.get}"

  puts "\nget_vertices"
  output = graph_data.get_vertices
  puts "  Output value: #{output}"

  puts "\nsize"
  output = graph_data.size
  puts "  Output value: #{output}"

  puts "\nadd_edge"
  input_list = [['A', 'B', 4], ['B', 'C', 2], ['C', 'A', 3]]
  input_list.each do |input|
    puts "  Input value: #{input}"
    output = graph_data.add_edge(*input)
    puts "  Output value: #{output}"
  end
  puts "  Current data: #{graph_data.get}"

  puts "\nget_vertices"
  output = graph_data.get_vertices
  puts "  Output value: #{output}"

  puts "\nget_edges"
  output = graph_data.get_edges
  puts "  Output value: #{output}"

  puts "\nsize"
  output = graph_data.size
  puts "  Output value: #{output}"

  puts "\nget_vertice"
  input = 'B'
  puts "  Input value: '#{input}'"
  output = graph_data.get_vertice(input)
  puts "  Output value: #{output}"

  puts "\nget_vertice"
  input = 'E'
  puts "  Input value: '#{input}'"
  output = graph_data.get_vertice(input)
  puts "  Output value: #{output}"

  puts "\nremove_edge"
  input = ['A', 'B']
  puts "  Input value: #{input}"
  output = graph_data.remove_edge(*input)
  puts "  Output value: #{output}"
  puts "  Current data: #{graph_data.get}"

  puts "\nremove_edge"
  input = ['A', 'C']
  puts "  Input value: #{input}"
  output = graph_data.remove_edge(*input)
  puts "  Output value: #{output}"
  puts "  Current data: #{graph_data.get}"

  puts "\nget_edges"
  output = graph_data.get_edges
  puts "  Output value: #{output}"

  puts "\nremove_vertex"
  input = 'B'
  puts "  Input value: #{input}"
  output = graph_data.remove_vertex(input)
  puts "  Output value: #{output}"
  puts "  Current data: #{graph_data.get}"

  puts "\nremove_vertex"
  input = 'Z'
  puts "  Input value: #{input}"
  output = graph_data.remove_vertex(input)
  puts "  Output value: #{output}"
  puts "  Current data: #{graph_data.get}"

  puts "\nget_vertices"
  output = graph_data.get_vertices
  puts "  Output value: #{output}"

  puts "\nget_edges"
  output = graph_data.get_edges
  puts "  Output value: #{output}"

  puts "\nsize"
  output = graph_data.size
  puts "  Output value: #{output}"

  puts "\nget_vertice"
  input = 'B'
  puts "  Input value: #{input}"
  output = graph_data.get_vertice(input)
  puts "  Output value: #{output}"

  puts "\nclear"
  output = graph_data.clear
  puts "  Output value: #{output}"

  puts "\nis_empty?"
  output = graph_data.is_empty?
  puts "  Output value: #{output}"

  puts "\nsize"
  output = graph_data.size
  puts "  Output value: #{output}"

  puts "\nget_vertices"
  output = graph_data.get_vertices
  puts "  Output value: #{output}"

  puts "\nget_edges"
  output = graph_data.get_edges
  puts "  Output value: #{output}"

  puts "\nGraph TEST <----- end"
end

# Only run main if the script is run directly
main if __FILE__ == $0
