# Ruby
# 素数判定: ミラーラビン (Miller-Rabin)

def is_prime(target, k=40)
  # 特殊なケースを処理
  return false if target <= 1
  return true if target <= 3
  return false if target % 2 == 0
  
  # n-1 = 2^r * d の形に分解する（dは奇数）
  r, d = 0, target - 1
  while d % 2 == 0
    r += 1
    d /= 2
  end
  
  # k回の試行を実行
  k.times do
    a = rand(2..target-2)
    x = a.pow(d, target)  # a^d mod n を計算
    
    next if x == 1 || x == target - 1
    
    is_composite = true
    (r - 1).times do
      x = x.pow(2, target)
      if x == target - 1
        is_composite = false
        break
      end
    end
    
    return false if is_composite  # 合成数と確定
  end
  
  true  # 素数である可能性が高い
end

def main
  puts "MillerRabin TEST -----> start"

  puts "\nis_prime"
  input_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 97, 100, 101, 997]
  input_list.each do |input|
    output = is_prime(input)
    puts "  #{input}: #{output}"
  end
  
  puts "\nMillerRabin TEST <----- end"
end

if __FILE__ == $0
  main
end