# Ruby
# 配列の並び替え: ヒープソート (Heap Sort)

class HeapData
  def initialize(is_min_heap = true)
    @data = []
    # 最小ヒープか最大ヒープかを設定
    @is_min_heap = is_min_heap
  end

  def _get_parent_idx(idx)
    # 親ノードのインデックスを計算
    return -1 if idx <= 0 # 根ノードには親がない
    (idx - 1) / 2
  end

  def _get_left_child_idx(idx)
    # 左の子ノードのインデックスを計算
    2 * idx + 1
  end

  def _get_right_child_idx(idx)
    # 右の子ノードのインデックスを計算
    2 * idx + 2
  end

  def _has_parent(idx)
    # 親ノードが存在するか確認
    _get_parent_idx(idx) >= 0 && _get_parent_idx(idx) < @data.length # len check added for robustness
  end

  def _has_left_child(idx)
    # 左の子ノードが存在するか確認
    _get_left_child_idx(idx) < @data.length
  end

  def _has_right_child(idx)
    # 右の子ノードが存在するか確認
    _get_right_child_idx(idx) < @data.length
  end

  def _swap(idx1, idx2)
    # 2つのノードの値を交換
    if 0 <= idx1 && idx1 < @data.length && 0 <= idx2 && idx2 < @data.length
      @data[idx1], @data[idx2] = @data[idx2], @data[idx1]
    else
      puts "Warning: Swap indices out of bounds: #{idx1}, #{idx2}" # Optional warning
    end
  end

  def _should_swap(idx1, idx2)
    # 最小ヒープでは idx1(親) が idx2(子) より大きい場合に交換
    # 最大ヒープでは idx1(親) が idx2(子) より小さい場合に交換
    if @is_min_heap
      @data[idx1] > @data[idx2]
    else # Max heap
      @data[idx1] < @data[idx2]
    end
  end

  def _heapify_down(idx)
    # ノードを下方向に移動させてヒープ条件を満たす (is_min_heap に従う)
    smallest_or_largest = idx

    left_child_idx = _get_left_child_idx(idx)
    right_child_idx = _get_right_child_idx(idx)

    # 左の子ノードと比較 (存在する場合)
    if _has_left_child(idx)
      if @is_min_heap # Min-heap: compare with smallest child
        if @data[left_child_idx] < @data[smallest_or_largest]
          smallest_or_largest = left_child_idx
        end
      else # Max-heap: compare with largest child
        if @data[left_child_idx] > @data[smallest_or_largest]
          smallest_or_largest = left_child_idx
        end
      end
    end

    # 右の子ノードと比較 (存在する場合)
    if _has_right_child(idx)
      if @is_min_heap # Min-heap: compare with smallest child
        if @data[right_child_idx] < @data[smallest_or_largest]
          smallest_or_largest = right_child_idx
        end
      else # Max-heap: compare with largest child
        if @data[right_child_idx] > @data[smallest_or_largest]
          smallest_or_largest = right_child_idx
        end
      end
    end

    # インデックスが変わっていたら交換して再帰的に処理
    if smallest_or_largest != idx
      _swap(idx, smallest_or_largest)
      _heapify_down(smallest_or_largest)
    end
  end

  def _max_heapify_down(idx, heap_size)
    # 指定されたヒープサイズ内で、idx から下方向に最大ヒープ条件を満たすように調整する。
    largest = idx # 最大ヒープでは、親と子の間で最大のものを探す

    left_child_idx = _get_left_child_idx(idx)
    right_child_idx = _get_right_child_idx(idx)

    # 左の子ノードと比較 (ヒープサイズ内に存在する場合)
    if left_child_idx < heap_size && @data[left_child_idx] > @data[largest]
      largest = left_child_idx
    end

    # 右の子ノードと比較 (ヒープサイズ内に存在する場合)
    if right_child_idx < heap_size && @data[right_child_idx] > @data[largest]
      largest = right_child_idx
    end

    # largest が現在のノード idx と異なる場合、交換して再帰的に処理
    if largest != idx
      _swap(idx, largest)
      _max_heapify_down(largest, heap_size)
    end
  end

  def _heapify_up(idx)
    # ノードを上方向に移動させてヒープ条件を満たす (is_min_heap に従う)
    while _has_parent(idx)
      parent_idx = _get_parent_idx(idx)
      if _should_swap(parent_idx, idx)
        _swap(parent_idx, idx)
        idx = parent_idx
      else
        break # Heap property satisfied
      end
    end
  end

  def get
    # 要素を取得 (現在の internal data を返す)
    @data
  end

  def heapify(array)
    @data = array
    n = array.length
    # 最後の非葉ノードから根に向かって、各部分木をヒープ化
    (n / 2 - 1).downto(0) do |i|
      _heapify_down(i) # Use the regular heapify_down
    end
    true
  end

  def sort
    n = @data.length

    # 配列を最大ヒープに変換する
    (n / 2 - 1).downto(0) do |i|
      _max_heapify_down(i, n)
    end

    # 最大ヒープから要素を一つずつ取り出してソート済みの位置に移動
    (n - 1).downto(1) do |i|
      # 現在の根 (最大値) をヒープの最後の要素と交換
      _swap(0, i)

      # 交換された要素 (元の最大値) は正しい位置に置かれたので、
      # 次のヒープ調整からは除外する。ヒープサイズは i に減少。
      # 新しい根 (元のヒープの最後の要素だったもの) から
      # _max_heapify_down を使って残りの要素を最大ヒープに調整
      _max_heapify_down(0, i)
    end
    
    true
  end
end

def main
  puts "HeapSort TEST -----> start"

  heap_data = HeapData.new

  # ランダムな整数の配列
  puts "\nsort"
  input = [64, 34, 25, 12, 22, 11, 90]
  puts "  ソート前: #{input}"
  heap_data.heapify(input)
  heap_data.sort
  puts "  ソート後: #{heap_data.get}"
  
  # 既にソートされている配列
  puts "\nsort"
  input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
  puts "  ソート前: #{input}"
  heap_data.heapify(input)
  heap_data.sort
  puts "  ソート後: #{heap_data.get}"
  
  # 逆順の配列
  puts "\nsort"
  input = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
  puts "  ソート前: #{input}"
  heap_data.heapify(input)
  heap_data.sort
  puts "  ソート後: #{heap_data.get}"
  
  # 重複要素を含む配列
  puts "\nsort"
  input = [10, 9, 8, 7, 6, 10, 9, 8, 7, 6]
  puts "  ソート前: #{input}"
  heap_data.heapify(input)
  heap_data.sort
  puts "  ソート後: #{heap_data.get}"
  
  # 空の配列
  puts "\nsort"
  input = []
  puts "  ソート前: #{input}"
  heap_data.heapify(input)
  heap_data.sort
  puts "  ソート後: #{heap_data.get}"

  puts "\nHeapSort TEST <----- end"
end

if __FILE__ == $0
  main
end