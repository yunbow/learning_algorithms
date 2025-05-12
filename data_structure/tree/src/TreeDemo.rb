# Ruby
# データ構造: 木 (Tree)

class NodeData
  def initialize(value)
    @value = value
    @parent = nil
    @children = []
  end
  
  def get_value
    @value
  end

  def get_parent
    @parent
  end

  def get_children
    @children
  end

  def set_parent(parent)
    @parent = parent
    true
  end

  def add_child(child)
    child.set_parent(self)
    @children << child
    true
  end
  
  def remove_child(child)
    if @children.include?(child)
      child.set_parent(nil)
      @children.delete(child)
      true
    else
      false
    end
  end
  
  def is_leaf
    @children.empty?
  end
end

class TreeData
  def initialize
    @data = nil
  end

  def get
    @data
  end
  
  def get_height(node = nil)
    node ||= @data
    return 0 if node.nil?
    return 1 if node.is_leaf
    1 + node.get_children.map { |child| get_height(child) }.max
  end
  
  def get_parent(node)
    node.get_parent
  end
  
  def get_children(node)
    node.get_children
  end

  def get_node(value, node = nil)
    node ||= @data
    return nil if node.nil?
    
    return node if node.get_value == value
    
    node.get_children.each do |child|
      result = get_node(value, child)
      return result if result
    end
    
    nil
  end

  def add(parent, value)
    new_node = NodeData.new(value)
    if parent.nil?
      if @data.nil?
        @data = new_node
        true
      else
        puts "ERROR: #{value} 重複です"
        false
      end
    else
      parent.add_child(new_node)
      true
    end
  end

  def remove(node)
    return false if node.nil?
    
    if node == @data
      @data = nil
      return true
    end
    
    parent = node.get_parent
    parent&.remove_child(node)
  end
  
  def traverse(node = nil, mode = "pre-order")
    node ||= @data
    return [] if node.nil?
    
    result = []
    
    case mode
    when "pre-order"
      result << node.get_value
      node.get_children.each do |child|
        result.concat(traverse(child, mode))
      end
    when "post-order"
      node.get_children.each do |child|
        result.concat(traverse(child, mode))
      end
      result << node.get_value
    when "level-order"
      queue = [node]
      until queue.empty?
        current = queue.shift
        result << current.get_value
        queue.concat(current.get_children)
      end
    end
    
    result
  end
  
  def is_leaf(node)
    !node.nil? && node.is_leaf
  end
  
  def is_empty
    @data.nil?
  end
  
  def size(node = nil)
    node ||= @data
    return 0 if node.nil?
    
    count = 1
    node.get_children.each do |child|
      count += size(child)
    end
    
    count
  end
  
  def clear
    @data = nil
    true
  end

  def display
    return [] if @data.nil?
    
    traverse(mode: "level-order")
  end
end

def main
  puts "Tree TEST -----> start"

  puts "\nnew"
  tree_data = TreeData.new
  puts "  現在のデータ: #{tree_data.display}"

  puts "\nis_empty"
  output = tree_data.is_empty
  puts "  出力値: #{output}"

  puts "\nsize"
  output = tree_data.size
  puts "  出力値: #{output}"

  puts "\nadd"
  input_params = [nil, 'Root']
  puts "  入力値: #{input_params}"
  output = tree_data.add(*input_params)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{tree_data.display}"
  
  root_node = tree_data.get

  puts "\nadd"
  input_params = [root_node, 'Child1']
  puts "  入力値: #{input_params}"
  output = tree_data.add(*input_params)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{tree_data.display}"

  puts "\nadd"
  input_params = [root_node, 'Child2']
  puts "  入力値: #{input_params}"
  output = tree_data.add(*input_params)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{tree_data.display}"

  puts "\nget_node"
  input_value = 'Child1'
  puts "  入力値: #{input_value}"
  output = tree_data.get_node(input_value)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{tree_data.display}"

  puts "\ntraverse"
  input_mode = 'pre-order'
  puts "  入力値: #{input_mode}"
  output = tree_data.traverse(mode: input_mode)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{tree_data.display}"

  puts "\nTree TEST <----- end"
end

main if __FILE__ == $PROGRAM_NAME