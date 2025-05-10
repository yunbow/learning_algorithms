# Ruby
# 配列の検索: ハッシュ探索 (Hash Search)

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
    # ハッシュテーブルの作成
    hash_table = {}
    
    # 配列の要素をハッシュテーブルに格納
    # キーを要素の値、値をインデックスとする
    @data.each_with_index do |value, i|
      hash_table[value] = i
    end
    
    # ハッシュテーブルを使って検索
    if hash_table.key?(target)
      hash_table[target]
    else
      -1
    end
  end
end

def main
  puts "HashSearch TEST -----> start"

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

  puts "\nHashSearch TEST <----- end"
end

if __FILE__ == $0
  main
end