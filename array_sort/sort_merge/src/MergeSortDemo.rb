# Ruby
# 配列の並び替え: マージソート (Merge Sort)

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

  def _merge_sort(target)
    # 配列の長さが1以下の場合はそのまま返す（基本ケース）
    return target if target.length <= 1
    
    # 配列を半分に分割
    mid = target.length / 2
    left_half = target[0...mid]
    right_half = target[mid..-1]
    
    # 左右の半分を再帰的にソート
    left_half = _merge_sort(left_half)
    right_half = _merge_sort(right_half)
    
    # ソート済みの半分同士をマージ
    _merge(left_half, right_half)
  end

  def _merge(left, right)
    result = []
    i = j = 0
    
    # 左右の配列を比較しながらマージ
    while i < left.length && j < right.length
      if left[i] <= right[j]
        result << left[i]
        i += 1
      else
        result << right[j]
        j += 1
      end
    end
    
    # 残った要素を追加
    result.concat(left[i..-1] || [])
    result.concat(right[j..-1] || [])
    
    result
  end

  def sort
    @data = _merge_sort(@data)
    true
  end
end

def main
  puts "MergeSort TEST -----> start"

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

  puts "\nMergeSort TEST <----- end"
end

if __FILE__ == $0
  main
end