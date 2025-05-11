# Ruby
# 配列の並び替え: 挿入ソート (Insertion Sort)

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
    # 配列の長さを取得
    n = @data.length
    
    # 2番目の要素から始める（最初の要素は既にソート済みと見なす）
    (1...n).each do |i|
      # 現在の要素を取得
      key = @data[i]
      
      # ソート済み部分の最後の要素のインデックス
      j = i - 1
      
      # keyより大きい要素をすべて右にシフト
      while j >= 0 && @data[j] > key
        @data[j + 1] = @data[j]
        j -= 1
      end
      
      # 適切な位置にkeyを挿入
      @data[j + 1] = key
    end
    
    true
  end
end

def main
  puts "InsertionSort TEST -----> start"

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

  puts "\nInsertionSort TEST <----- end"
end

if __FILE__ == $0
  main
end