# Ruby
# 配列の並び替え: クイックソート (Quick Sort)

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

  def _quick_sort(target)
    # 空の配列または要素が1つの場合はそのまま返す（基底条件）
    return target if target.length <= 1
    
    # ピボットを選択（この実装では最後の要素を選択）
    pivot = target[-1]
    
    # ピボットより小さい要素と大きい要素に分ける
    left = []
    right = []
    
    # 最後の要素（ピボット）を除いて配列をスキャン
    (0...target.length - 1).each do |i|
      if target[i] <= pivot
        left << target[i]
      else
        right << target[i]
      end
    end
    
    # 左側の部分配列、ピボット、右側の部分配列を再帰的にソートして結合
    _quick_sort(left) + [pivot] + _quick_sort(right)
  end

  def sort
    @data = _quick_sort(@data)
    true
  end
end

def main
  puts "QuickSort TEST -----> start"

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

  puts "\nQuickSort TEST <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end