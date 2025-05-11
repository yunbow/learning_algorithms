# Ruby
# 配列の並び替え: バブルソート (Bubble Sort)

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

  def sort
    n = @data.length
    
    # 外側のループ: n-1回の走査が必要
    n.times do |i|
      # 最適化: 一度の走査で交換がなければソート完了
      swapped = false
      
      # 内側のループ: まだソートされていない部分を走査
      # 各走査後に最大の要素が末尾に移動するため、i回分を除外
      (0...(n-i-1)).each do |j|
        # 隣接する要素を比較し、必要に応じて交換
        if @data[j] > @data[j+1]
          @data[j], @data[j+1] = @data[j+1], @data[j]
          swapped = true
        end
      end
      
      # 交換が発生しなければソート完了
      break unless swapped
    end
    true
  end
end

def main
  puts "BubbleSort TEST -----> start"

  array_data = ArrayData.new

  # ランダムな整数の配列
  puts "\nsort"
  input = [64, 34, 25, 12, 22, 11, 90]
  puts "  ソート前: #{input}"
  array_data.set(input)
  array_data.sort
  puts "  ソート後: #{array_data.get}"
  
  # 既にソートされている配列
  puts "\nsort"
  input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
  puts "  ソート前: #{input}"
  array_data.set(input)
  array_data.sort
  puts "  ソート後: #{array_data.get}"
  
  # 逆順の配列
  puts "\nsort"
  input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
  puts "  ソート前: #{input}"
  array_data.set(input)
  array_data.sort
  puts "  ソート後: #{array_data.get}"
  
  # 重複要素を含む配列
  puts "\nsort"
  input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6]
  puts "  ソート前: #{input}"
  array_data.set(input)
  array_data.sort
  puts "  ソート後: #{array_data.get}"
  
  # 空の配列
  puts "\nsort"
  input = []
  puts "  ソート前: #{input}"
  array_data.set(input)
  array_data.sort
  puts "  ソート後: #{array_data.get}"

  puts "\nBubbleSort TEST <----- end"
end

if __FILE__ == $0
  main
end