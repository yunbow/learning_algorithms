# Ruby
# データ構造: ヒープ (Heap)

class HeapData
  def initialize(is_min_heap = true)
    @data = []
    @is_min_heap = is_min_heap
  end

  def _get_parent_idx(idx)
    (idx - 1) / 2
  end

  def _get_left_child_idx(idx)
    2 * idx + 1
  end

  def _get_right_child_idx(idx)
    2 * idx + 2
  end

  def _has_parent?(idx)
    _get_parent_idx(idx) >= 0
  end

  def _has_left_child?(idx)
    _get_left_child_idx(idx) < @data.length
  end

  def _has_right_child?(idx)
    _get_right_child_idx(idx) < @data.length
  end

  def _get_parent(idx)
    @data[_get_parent_idx(idx)]
  end

  def _get_left_child(idx)
    @data[_get_left_child_idx(idx)]
  end

  def _get_right_child(idx)
    @data[_get_right_child_idx(idx)]
  end

  def _swap(idx1, idx2)
    @data[idx1], @data[idx2] = @data[idx2], @data[idx1]
  end

  def _should_swap?(idx1, idx2)
    if @is_min_heap
      @data[idx1] > @data[idx2]
    else
      @data[idx1] < @data[idx2]
    end
  end

  def _heapify_down(idx)
    smallest_or_largest = idx

    if _has_left_child?(idx) && _should_swap?(_get_left_child_idx(idx), smallest_or_largest)
      smallest_or_largest = _get_left_child_idx(idx)
    end

    if _has_right_child?(idx) && _should_swap?(_get_right_child_idx(idx), smallest_or_largest)
      smallest_or_largest = _get_right_child_idx(idx)
    end

    if smallest_or_largest != idx
      _swap(idx, smallest_or_largest)
      _heapify_down(smallest_or_largest)
    end
  end

  def _heapify_up(idx)
    while _has_parent?(idx) && _should_swap?(_get_parent_idx(idx), idx)
      parent_idx = _get_parent_idx(idx)
      _swap(parent_idx, idx)
      idx = parent_idx
    end
  end

  def get
    @data
  end

  def get_index(item)
    begin
      @data.index(item)
    rescue
      puts "ERROR: #{item} は範囲外です"
      -1
    end
  end

  def get_value(index)
    if index >= 0 && index < @data.length
      @data[index]
    else
      puts "ERROR: #{index} は範囲外です"
      nil
    end
  end

  def heapify(array)
    @data = array.dup
    (@data.length / 2 - 1).downto(0) do |i|
      _heapify_down(i)
    end
    true
  end

  def push(value)
    @data << value
    _heapify_up(@data.length - 1)
    true
  end

  def pop
    return false if @data.empty?

    last_element = @data.pop

    if @data.length > 0
      @data[0] = last_element
      _heapify_down(0)
    end

    true
  end

  def peek
    return nil if @data.empty?
    @data[0]
  end

  def empty?
    @data.empty?
  end

  def size
    @data.length
  end

  def clear
    @data = []
    true
  end
end

def main
  puts "Heap TEST -----> start"

  puts "\nmin heap: new"
  min_heap = HeapData.new(true)
  puts "  現在のデータ: #{min_heap.get}"

  puts "\nmin heap: heapify"
  input = [4, 10, 3, 5, 1]
  puts "  入力値: #{input}"
  output = min_heap.heapify(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{min_heap.get}"

  puts "\nmin heap: push"
  input = 2
  puts "  入力値: #{input}"
  output = min_heap.push(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{min_heap.get}"

  puts "\nmin heap: push"
  input = 15
  puts "  入力値: #{input}"
  output = min_heap.push(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{min_heap.get}"

  puts "\nmin heap: peek"
  output = min_heap.peek
  puts "  出力値: #{output}"

  puts "\nmin heap: pop"
  output = min_heap.pop
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{min_heap.get}"

  puts "\nmin heap: pop"
  output = min_heap.pop
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{min_heap.get}"

  puts "\nmin heap: get_index"
  input = 3
  puts "  入力値: #{input}"
  output = min_heap.get_index(input)
  puts "  出力値: #{output}"

  puts "\nmin heap: get_index"
  input = 100
  puts "  入力値: #{input}"
  output = min_heap.get_index(input)
  puts "  出力値: #{output}"

  puts "\nmin heap: is_empty"
  output = min_heap.empty?
  puts "  出力値: #{output}"

  puts "\nmin heap: size"
  output = min_heap.size
  puts "  出力値: #{output}"

  puts "\nmin heap: clear"
  output = min_heap.clear
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{min_heap.get}"

  puts "\nmin heap: is_empty"
  output = min_heap.empty?
  puts "  出力値: #{output}"

  puts "\nmax heap: new"
  max_heap = HeapData.new(false)
  puts "  現在のデータ: #{max_heap.get}"

  puts "\nmax heap: heapify"
  input = [4, 10, 3, 5, 1]
  puts "  入力値: #{input}"
  output = max_heap.heapify(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{max_heap.get}"

  puts "\nmax heap: push"
  input = 12
  puts "  入力値: #{input}"
  output = max_heap.push(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{max_heap.get}"

  puts "\nmax heap: push"
  input = 0
  puts "  入力値: #{input}"
  output = max_heap.push(input)
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{max_heap.get}"

  puts "\nmax heap: peek"
  output = max_heap.peek
  puts "  出力値: #{output}"

  puts "\nmax heap: pop"
  output = max_heap.pop
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{max_heap.get}"

  puts "\nmax heap: pop"
  output = max_heap.pop
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{max_heap.get}"

  puts "\nmax heap: get_index"
  input = 5
  puts "  入力値: #{input}"
  output = max_heap.get_index(input)
  puts "  出力値: #{output}"

  puts "\nmax heap: get_index"
  input = -10
  puts "  入力値: #{input}"
  output = max_heap.get_index(input)
  puts "  出力値: #{output}"

  puts "\nmax heap: is_empty"
  output = max_heap.empty?
  puts "  出力値: #{output}"

  puts "\nmax heap: size"
  output = max_heap.size
  puts "  出力値: #{output}"

  puts "\nmax heap: clear"
  output = max_heap.clear
  puts "  出力値: #{output}"
  puts "  現在のデータ: #{max_heap.get}"

  puts "\nmax heap: is_empty"
  output = max_heap.empty?
  puts "  出力値: #{output}"

  puts "\nHeap TEST <----- end"
end

main if __FILE__ == $PROGRAM_NAME