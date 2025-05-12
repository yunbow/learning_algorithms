# Ruby
# データ構造: マップ (Map)

class MapData
  def initialize
    @data = {}
  end

  def get
    @data.to_a
  end
  
  def get_keys
    @data.keys
  end
  
  def get_values
    @data.values
  end
  
  def get_key(value)
    @data.key(value)
  end
  
  def get_value(key)
    @data[key]
  end

  def add(key, value)
    if @data.key?(key)
      puts "ERROR: #{key} は重複です"
      false
    else
      @data[key] = value
      true
    end
  end
  
  def remove(key)
    if @data.key?(key)
      @data.delete(key)
      true
    else
      puts "ERROR: #{key} は範囲外です"
      false
    end
  end
  
  def update(key, value)
    if @data.key?(key)
      @data[key] = value
      true
    else
      puts "ERROR: #{key} は範囲外です"
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
  puts "Map TEST -----> start"

  puts "\nnew"
  map_data = MapData.new
  puts "  現在のデータ: #{map_data.get}"

  puts "\nis_empty?"
  output = map_data.is_empty?
  puts "  出力値: #{output}"

  puts "\nsize"
  output = map_data.size
  puts "  出力値: #{output}"

  puts "\nadd"
  input = ['apple', 100]
  puts "  入力値: #{input}"
  output = map_data.add(*input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nadd"
  input = ['banana', 150]
  puts "  入力値: #{input}"
  output = map_data.add(*input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nadd"
  input = ['apple', 200]
  puts "  入力値: #{input}"
  output = map_data.add(*input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nsize"
  output = map_data.size
  puts "  出力値: #{output}"

  puts "\nget"
  input = 'apple'
  puts "  入力値: #{input}"
  output = map_data.get_value(input)
  puts "  出力値: #{output}"

  puts "\nget"
  input = 'orange'
  puts "  入力値: #{input}"
  output = map_data.get_value(input)
  puts "  出力値: #{output}"

  puts "\nupdate"
  input = ['banana', 180]
  puts "  入力値: #{input}"
  output = map_data.update(*input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nupdate"
  input = ['orange', 250]
  puts "  入力値: #{input}"
  output = map_data.update(*input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nget"
  input = 'banana'
  output = map_data.get_value(input)
  puts "  出力値: #{output}"

  puts "\nget_keys"
  output = map_data.get_keys
  puts "  出力値: #{output}"

  puts "\nvalues"
  output = map_data.get_values
  puts "  出力値: #{output}"

  puts "\nget_key"
  input = 180
  puts "  入力値: #{input}"
  output = map_data.get_key(input)
  puts "  出力値: #{output}"

  puts "\nget_key"
  input = 500
  puts "  入力値: #{input}"
  output = map_data.get_key(input)
  puts "  出力値: #{output}"

  puts "\nremove"
  input = 'apple'
  puts "  入力値: #{input}"
  output = map_data.remove(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nremove"
  input = 'orange'
  puts "  入力値: #{input}"
  output = map_data.remove(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nsize"
  output = map_data.size
  puts "  出力値: #{output}"

  puts "\nget_keys"
  output = map_data.get_keys
  puts "  出力値: #{output}"

  puts "\nclear"
  output = map_data.clear
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{map_data.get}"

  puts "\nsize"
  output = map_data.size
  puts "  出力値: #{output}"

  puts "\nis_empty?"
  output = map_data.is_empty?
  puts "  出力値: #{output}"

  puts "\nMap TEST <----- end"
end

main if __FILE__ == $PROGRAM_NAME