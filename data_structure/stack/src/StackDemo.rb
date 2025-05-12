# Ruby
# データ構造: スタック (Stack)

class StackData
  def initialize
    @data = []
  end

  def get
    @data
  end

  def get_index(item)
    begin
      index = @data.index(item)
      return index
    rescue
      puts "ERROR: #{item} は範囲外です"
      return -1
    end
  end

  def get_value(index)
    if 0 <= index && index < @data.length
      @data[index]
    else
      puts "ERROR: #{index} は範囲外です"
      nil
    end
  end
  
  def push(item)
    @data.push(item)
    true
  end
  
  def pop
    if !is_empty
      @data.pop
      true
    else
      puts "ERROR: 空です"
      false
    end
  end

  def peek
    if !is_empty
      @data[-1]
    else
      nil
    end
  end
  
  def is_empty
    @data.empty?
  end
  
  def size
    @data.length
  end
  
  def clear
    @data = []
    true
  end
end

def main
  puts "Stack TEST -----> start"

  puts "\nnew"
  stack_data = StackData.new
  puts "  現在のデータ: #{stack_data.get}"

  puts "\nis_empty"
  output = stack_data.is_empty
  puts "  出力値: #{output}"

  puts "\nsize"
  output = stack_data.size
  puts "  出力値: #{output}"

  puts "\npush"
  items_to_push = [10, 20, 30, 40]
  items_to_push.each do |item|
    puts "  入力値: #{item}"
    output = stack_data.push(item)
    puts "  出力値: #{output}"
    puts "  現在のデータ: #{stack_data.get}"
  end

  puts "\nsize"
  output = stack_data.size
  puts "  出力値: #{output}"

  puts "\nis_empty"
  output = stack_data.is_empty
  puts "  出力値: #{output}"

  puts "\npeek"
  output = stack_data.peek
  puts "  出力値: #{output}"

  puts "\nget_index"
  input = 30
  puts "  入力値: #{input}"
  output = stack_data.get_index(input)
  puts "  出力値: #{output}"

  puts "\nget_index"
  input = 50
  puts "  入力値: #{input}"
  output = stack_data.get_index(input)
  puts "  出力値: #{output}"

  puts "\npop"
  until stack_data.is_empty
    output = stack_data.pop
    puts "  出力値: #{output}"
    puts "  現在のデータ: #{stack_data.get}"
  end

  puts "\nis_empty"
  output = stack_data.is_empty
  puts "  出力値: #{output}"

  puts "\nsize"
  output = stack_data.size
  puts "  出力値: #{output}"

  puts "\npop"
  output = stack_data.pop
  puts "  出力値: #{output}"

  puts "\npeek"
  output = stack_data.peek
  puts "  出力値: #{output}"

  puts "\nStack TEST <----- end"
end

if __FILE__ == $0
  main
end