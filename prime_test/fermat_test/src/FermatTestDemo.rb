# Ruby
# 素数判定: フェルマーテスト (Fermat Test)

require 'securerandom'

def is_prime(target, k=5)
  return true if target == 2 || target == 3
  
  # 2未満または偶数は素数でない（2を除く）
  return false if target < 2 || target % 2 == 0
  
  k.times do
    # 1 < a < target の範囲からランダムに数を選ぶ
    a = SecureRandom.random_number(target - 3) + 2
    
    # フェルマーの小定理: a^(target-1) ≡ 1 (mod target) が成り立つか検証
    # a.pow(target - 1, target) は (a^(target-1)) % target を効率的に計算します。
    return false if a.pow(target - 1, target) != 1
  end
  
  true
end

def main
  puts "FermatTest TEST -----> start"
  
  puts "\nis_prime"
  input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
  input_list.each do |input|
    output = is_prime(input)
    puts "  #{input}: #{output}"
  end
  
  puts "\nFermatTest TEST <----- end"
end

if __FILE__ == $PROGRAM_NAME
  main
end