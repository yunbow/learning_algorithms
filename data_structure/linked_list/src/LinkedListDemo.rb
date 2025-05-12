# Ruby
# データ構造: 連結リスト (Linked List)

class NodeData
  attr_accessor :data, :next

  def initialize(data)
    @data = data
    @next = nil
  end
end

class LinkedListData
  def initialize
    @data = nil
    @size = 0
  end

  def get
    @data
  end

  def get_position(data)
    return -1 if empty?

    current = @data
    position = 0

    while current
      return position if current.data == data
      current = current.next
      position += 1
    end

    -1
  end

  def get_value(position)
    if empty? || position < 0 || position >= @size
      puts "ERROR: #{position} は範囲外です"
      return nil
    end

    current = @data
    position.times { current = current.next }

    current.data
  end

  def add(data, position = nil)
    new_node = NodeData.new(data)

    if empty?
      @data = new_node
      @size += 1
      return true
    end

    if position.nil? || position >= @size
      current = @data
      current = current.next while current.next
      current.next = new_node
      @size += 1
      return true
    end

    if position == 0
      new_node.next = @data
      @data = new_node
      @size += 1
      return true
    end

    current = @data
    (position - 1).times { current = current.next }

    new_node.next = current.next
    current.next = new_node
    @size += 1
    true
  end

  def remove(position: nil, data: nil)
    return false if empty?

    if !data.nil?
      if @data.data == data
        @data = @data.next
        @size -= 1
        return true
      end

      current = @data
      while current.next && current.next.data != data
        current = current.next
      end

      if current.next
        current.next = current.next.next
        @size -= 1
        return true
      else
        puts "ERROR: #{data} は範囲外です"
        return false
      end
    end

    position ||= @size - 1

    if position < 0 || position >= @size
      puts "ERROR: #{position} は範囲外です"
      return false
    end

    if position == 0
      @data = @data.next
      @size -= 1
      return true
    end

    current = @data
    (position - 1).times { current = current.next }

    current.next = current.next.next
    @size -= 1
    true
  end

  def update(position, data)
    if empty? || position < 0 || position >= @size
      puts "ERROR: #{position} は範囲外です"
      return false
    end

    current = @data
    position.times { current = current.next }

    current.data = data
    true
  end

  def empty?
    @data.nil?
  end

  def size
    @size
  end

  def clear
    @data = nil
    @size = 0
    true
  end

  def display
    elements = []
    current = @data
    while current
      elements << current.data
      current = current.next
    end
    elements
  end
end

def main
  puts "LinkedList TEST -----> start"

  puts "\nnew"
  linked_list_data = LinkedListData.new
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nis_empty"
  output = linked_list_data.empty?
  puts "  出力値: #{output}"

  puts "\nsize"
  output = linked_list_data.size
  puts "  出力値: #{output}"

  puts "\nadd"
  input = 10
  puts "  入力値: #{input}"
  output = linked_list_data.add(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nadd"
  input = 20
  puts "  入力値: #{input}"
  output = linked_list_data.add(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nadd"
  input = 5
  position = 0
  puts "  入力値: #{input}, #{position}"
  output = linked_list_data.add(input, position)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nadd"
  input = 15
  position = 2
  puts "  入力値: #{input}, #{position}"
  output = linked_list_data.add(input, position)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nget_position"
  input = 1
  puts "  入力値: #{input}"
  output = linked_list_data.get_position(input)
  puts "  出力値: #{output}"

  puts "\nget_position"
  input = 10
  puts "  入力値: #{input}"
  output = linked_list_data.get_position(input)
  puts "  出力値: #{output}"

  puts "\nupdate"
  position = 1
  input = 99
  puts "  入力値: #{position}, #{input}"
  output = linked_list_data.update(position, input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nget_value"
  input = 2
  puts "  入力値: #{input}"
  output = linked_list_data.get_value(input)
  puts "  出力値: #{output}"

  puts "\nget_value"
  input = 100
  puts "  入力値: #{input}"
  output = linked_list_data.get_value(input)
  puts "  出力値: #{output}"

  puts "\nremove"
  input = 15
  puts "  入力値: data=#{input}"
  output = linked_list_data.remove(data: input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nremove"
  input = 0
  puts "  入力値: position=#{input}"
  output = linked_list_data.remove(position: input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nremove"
  output = linked_list_data.remove
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nremove"
  input = 5
  puts "  入力値: position=#{input}"
  output = linked_list_data.remove(position: input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nclear"
  output = linked_list_data.clear
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nempty?"
  output = linked_list_data.empty?
  puts "  出力値: #{output}"

  puts "\nsize"
  output = linked_list_data.size
  puts "出力値: #{output}"

  puts "\nremove"
  output = linked_list_data.remove
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{linked_list_data.display}"

  puts "\nLinkedList TEST <----- end"
end

main if __FILE__ == $PROGRAM_NAME