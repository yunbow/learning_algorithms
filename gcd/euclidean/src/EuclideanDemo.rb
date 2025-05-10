# Ruby
# 最大公約数: ユークリッドの互除法 (Euclidean)

def gcd(a, b)
  # bが0になるまで繰り返し計算する
  while b != 0
    a, b = b, a % b
  end
  return a
end

def main
  puts "Euclidean TEST -----> start"
  input = [48, 18]
  puts "\n  入力値: #{input}"
  output = gcd(*input)
  puts "\n  出力値: #{output}"
  puts "\nEuclidean TEST <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end