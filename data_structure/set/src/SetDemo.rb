# Ruby
# データ構造: セット (Set)

require 'set'

class SetData
  def initialize
    @data = Set.new
  end

  def get
    @data.to_a
  end

  def get_index(item)
    index = get.index(item)
    if index
      index
    else
      puts "ERROR: #{item} は範囲外です"
      -1
    end
  end

  def get_value(index)
    if index >= 0 && index < @data.size
      get[index]
    else
      puts "ERROR: #{index} は範囲外です"
      nil
    end
  end

  def add(item)
    if @data.add?(item)
      true
    else
      puts "ERROR: #{item} は重複です"
      false
    end
  end

  def remove(item)
    if @data.delete?(item)
      true
    else
      puts "ERROR: #{item} は範囲外です"
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
  puts "Set TEST -----> start"

  puts "\nnew"
  set_data = SetData.new
  puts "  現在のデータ: #{set_data.get}"

  puts "\nadd"
  input = [10, 20, 30, 20, 40]
  input.each do |item|
    puts "  入力値: #{item}"
    output = set_data.add(item)
    puts "  出力値: #{output}"
    puts "  現在のデータ: #{set_data.get}"
  end

  puts "\nsize"
  output = set_data.size
  puts "  出力値: #{output}"

  puts "\nis_empty?"
  output = set_data.is_empty?
  puts "  出力値: #{output}"

  puts "\nget_value"
  input = [0, 2, 5]
  input.each do |index|
    puts "  入力値: #{index}"
    output = set_data.get_value(index)
    puts "  出力値: #{output}"
  end

  puts "\nget_index"
  input = [30, 99]
  input.each do |item|
    puts "  入力値: #{item}"
    output = set_data.get_index(item)
    puts "  出力値: #{output}"
  end

  puts "\nremove"
  input = [20, 50, 10]
  input.each do |item|
    puts "  入力値: #{item}"
    output = set_data.remove(item)
    puts "  出力値: #{output}"
    puts "  現在のデータ: #{set_data.get}"
  end

  puts "\nsize"
  output = set_data.size
  puts "  出力値: #{output}"

  puts "\nclear"
  output = set_data.clear
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{set_data.get}"

  puts "\nis_empty?"
  output = set_data.is_empty?
  puts "  出力値: #{output}"

  puts "\nSet TEST <----- end"
end

main
