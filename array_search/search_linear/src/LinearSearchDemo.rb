# Ruby
# 配列の検索: 線形探索 (Linear Search)

class ArrayData
  def initialize
    @data = []
  end

  def get
    @data
  end

  def set(data)
    @data = data
    true
  end

  def search(target)
    # 配列の要素を順番に確認
    @data.each_with_index do |value, index|
      # 目的の値が見つかった場合、そのインデックスを返す
      return index if value == target
    end
    
    # 見つからなかった場合は -1 を返す
    -1
  end
end

def main
  puts "LinearSearch TEST -----> start"

  puts "\nnew"
  array_data = ArrayData.new
  input = [1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
  array_data.set(input)
  puts "  現在のデータ: #{array_data.get}"
  
  puts "\nsearch"
  input = 7
  puts "  入力値: #{input}"
  output = array_data.search(input)
  puts "  出力値: #{output}"

  puts "\nsearch"
  input = 30
  puts "  入力値: #{input}"
  output = array_data.search(input)
  puts "  出力値: #{output}"

  puts "\nLinearSearch TEST <----- end"
end

if __FILE__ == $0
  main
end