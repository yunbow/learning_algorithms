# Ruby
# データ構造: キュー (Queue)

class QueueData
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
    rescue IndexError
      puts "ERROR: #{item} は範囲外です"
      -1
    end
  end

  def get_value(index)
    if index >= 0 && index < @data.length
      @data[index]
    else
      puts "Error: インデックス #{index} は範囲外です"
      nil
    end
  end

  def enqueue(item)
    @data.push(item)
    true
  end
  
  def dequeue
    unless is_empty?
      @data.shift
      true
    else
      puts "ERROR: キューが空です"
      false
    end
  end
  
  def peek
    unless is_empty?
      @data.first
    else
      puts "ERROR: キューが空です"
      nil
    end
  end
  
  def is_empty?
    @data.empty?
  end
  
  def size
    @data.length
  end
  
  def clear
    @data.clear
    true
  end
end

def main
  puts "Queue TEST -----> start"

  puts "\nnew"
  queue_data = QueueData.new
  puts "  現在のデータ: #{queue_data.get}"

  puts "\nis_empty"
  output = queue_data.is_empty?
  puts "  出力値: #{output}"

  puts "\nenqueue"
  input = [10, 20, 30]
  input.each do |item|
    puts "  入力値: #{item}"
    output = queue_data.enqueue(item)
    puts "  出力値: #{output}"
    puts "  現在のデータ: #{queue_data.get}"
  end

  puts "\nsize"
  output = queue_data.size
  puts "  出力値: #{output}"

  puts "\npeek"
  output = queue_data.peek
  puts "  出力値: #{output}"

  puts "\nget_index"
  input = 20
  puts "  入力値: #{input}"
  output = queue_data.get_index(input)
  puts "  出力値: #{output}"

  puts "\nget_index"
  input = 50
  puts "  入力値: #{input}"
  output = queue_data.get_index(input)
  puts "  出力値: #{output}"

  puts "\ndequeue"
  output = queue_data.dequeue
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{queue_data.get}"

  puts "\ndequeue"
  output = queue_data.dequeue
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{queue_data.get}"

  puts "\nsize"
  output = queue_data.size
  puts "  出力値: #{output}"

  puts "\ndequeue"
  output = queue_data.dequeue
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{queue_data.get}"

  puts "\ndequeue"
  output = queue_data.dequeue
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{queue_data.get}"

  puts "\nis_empty"
  output = queue_data.is_empty?
  puts "  出力値: #{output}"

  puts "\nclear"
  output = queue_data.clear
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{queue_data.get}"

  puts "\nsize"
  output = queue_data.size
  puts "  出力値: #{output}"

  puts "\nQueue TEST <----- end"
end

main if __FILE__ == $0