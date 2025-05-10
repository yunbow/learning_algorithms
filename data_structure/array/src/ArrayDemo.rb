# Ruby
# データ構造: 配列 (Array)

class ArrayData
  def initialize
    @data = []
  end
  
  def get
    # 要素を取得
    @data
  end

  def get_index(item)
    # 配列内で指定された値を検索し、最初に見つかったインデックスを返す
    begin
      index = @data.index(item)
      return index
    rescue
      puts "ERROR: #{item} は範囲外です"
      return -1
    end
  end

  def get_value(index)
    # 指定されたインデックスの要素を取得する
    if 0 <= index && index < @data.length
      @data[index]
    else
      puts "ERROR: #{index} は範囲外です"
      nil
    end
  end

  def add(item)
    # 配列の末尾に要素を追加する
    @data.push(item)
    true
  end
  
  def remove(index)
    # 指定されたインデックスの要素を削除する
    if 0 <= index && index < @data.length
      @data.delete_at(index)
      true
    else
      puts "ERROR: #{index} は範囲外です"
      false
    end
  end
  
  def update(index, new_value)
    # 指定されたインデックスの要素を新しい値に更新する
    if 0 <= index && index < @data.length
      @data[index] = new_value
      true
    else
      puts "ERROR: #{index} は範囲外です"
      false
    end
  end

  def reverse
    # 配列の要素を逆順にする
    @data.reverse!
    @data
  end
  
  def sort(descending = false)
    # 配列の要素をソートする
    if descending
      @data.sort! { |a, b| b <=> a }
    else
      @data.sort!
    end
    @data
  end
      
  def is_empty
    # 配列が空かどうか
    @data.empty?
  end
  
  def size
    # 配列のサイズ（要素数）を返す
    @data.length
  end
  
  def clear
    # 配列の全要素を削除する
    @data = []
    true
  end
end

def main
  puts "Array TEST -----> start"

  puts "\nnew"
  array_data = ArrayData.new
  puts "  現在のデータ: #{array_data.get}"

  puts "\nadd"
  input = [10, 20, 30, 10, 40]
  input.each do |item|
    puts "  入力値: #{item}"
    output = array_data.add(item)
    puts "  出力値: #{output}"
    puts "  現在のデータ: #{array_data.get}"
  end

  puts "\nsize"
  output = array_data.size
  puts "  出力値: #{output}"

  puts "\nis_empty"
  output = array_data.is_empty
  puts "  出力値: #{output}"

  puts "\nget_value"
  input = 2
  puts "  入力値: #{input}"
  output = array_data.get_value(input)
  puts "  出力値: #{output}"

  puts "\nget_value"
  input = 10
  puts "  入力値: #{input}"
  output = array_data.get_value(input)
  puts "  出力値: #{output}"

  puts "\nupdate"
  input = [1, 25]
  puts "  入力値: #{input}"
  output = array_data.update(*input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{array_data.get}"

  puts "\nupdate"
  input = [15, 25]
  puts "  入力値: #{input}"
  output = array_data.update(*input)
  puts "  出力値: #{output}" 
  puts "  現在のデータ: #{array_data.get}" 

  puts "\nget_index"
  input = 10
  puts "  入力値: #{input}"
  output = array_data.get_index(input)
  puts "  出力値: #{output}"

  puts "\nget_index"
  input = 99
  puts "  入力値: #{input}"
  output = array_data.get_index(input)
  puts "  出力値: #{output}"

  puts "\nremove"
  input = 3
  puts "  入力値: #{input}"
  output = array_data.remove(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{array_data.get}"

  puts "\nremove"
  input = 8
  puts "  入力値: #{input}"
  output = array_data.remove(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{array_data.get}"

  puts "\nreverse"
  output = array_data.reverse
  puts "  出力値: #{output}"

  puts "\nsort"
  puts "  入力値: descending=false"
  output = array_data.sort(false)
  puts "  出力値: #{output}"

  puts "\nsort"
  puts "  入力値: descending=true"
  output = array_data.sort(true)
  puts "  出力値: #{output}"

  puts "\nclear"
  output = array_data.clear
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{array_data.get}"

  puts "\nis_empty"
  output = array_data.is_empty
  puts "  出力値: #{output}"

  puts "\nArray TEST <----- end"
end

if __FILE__ == $0
  main
end