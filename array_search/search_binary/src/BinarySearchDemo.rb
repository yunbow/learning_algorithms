# Ruby
# 配列の検索: 二分探索 (Binary Search)

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
    left = 0
    right = @data.length - 1
    
    while left <= right
      mid = (left + right) / 2
      
      # 中央の要素が目標値と一致
      if @data[mid] == target
        return mid
      
      # 中央の要素が目標値より小さい場合、右半分を探索
      elsif @data[mid] < target
        left = mid + 1
      
      # 中央の要素が目標値より大きい場合、左半分を探索
      else
        right = mid - 1
      end
    end
    
    # 目標値が見つからない場合
    -1
  end
end

def main
  puts "BinarySearch TEST -----> start"

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

  puts "\nBinarySearch TEST <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end