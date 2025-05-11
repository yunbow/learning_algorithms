# Ruby
# 配列の並び替え: 選択ソート (Selection Sort)

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
    _selection_sort(@data)
    true
  end

  private

  def _selection_sort(target)
    # 配列の長さを取得
    n = target.length
    
    # 配列を順番に走査
    for i in 0...n
      # 未ソート部分の最小値のインデックスを見つける
      min_index = i
      for j in (i + 1)...n
        if target[j] < target[min_index]
          min_index = j
        end
      end
      
      # 見つかった最小値と現在の位置を交換
      target[i], target[min_index] = target[min_index], target[i]
    end
    
    target
  end
end

def main
  puts "SelectionSort TEST -----> start"

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

  puts "\nSelectionSort TEST <----- end"
end

if __FILE__ == $0
  main
end