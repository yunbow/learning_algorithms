# Python
# グラフの最短経路: A-star
import heapq

class GraphData:
    def __init__(self):
        # 隣接ノードとその辺の重みを格納します。
        # キーは頂点、値はその頂点に隣接する頂点と重みの辞書です。
        # 例: { 'A': {'B': 1, 'C': 4}, 'B': {'A': 1, 'C': 2, 'D': 5}, ... }
        self._data = {}

    def get(self):
        # グラフの内部データを取得します。
        return self._data

    def get_vertices(self):
        # グラフの全頂点をリストとして返します。
        return list(self._data.keys())

    def get_edges(self):
        # グラフの全辺をリストとして返します。
        # 無向グラフの場合、(u, v, weight) の形式で返します。
        # 重複を避けるためにセットを使用します。
        edges = set()
        for vertex in self._data:
            for neighbor, weight in self._data[vertex]:
                # 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                edge = tuple(sorted((vertex, neighbor)))
                edges.add((edge[0], edge[1], weight)) # (u, v, weight) の形式で格納
        return list(edges)

    def get_neighbors(self, vertex):
        # 指定された頂点の隣接ノードと辺の重みのリストを返します。
        # 形式: [(隣接頂点, 重み), ...]
        if vertex in self._data:
            return self._data[vertex]
        else:
            return None # 頂点が存在しない場合はNoneを返す

    def add_vertex(self, vertex):
        # 新しい頂点をグラフに追加します。
        if vertex not in self._data:
            self._data[vertex] = []
            return True
        # 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return True

    def add_edge(self, vertex1, vertex2, weight):
        # 両頂点間に辺を追加します。重みを指定します。
        # 頂点がグラフに存在しない場合は追加します。
        if vertex1 not in self._data:
            self.add_vertex(vertex1)
        if vertex2 not in self._data:
            self.add_vertex(vertex2)
        
        # 両方向に辺を追加する（無向グラフ）
        # 既に同じ辺が存在する場合は重みを更新するかどうか？
        # 今回は単純に追加する（重複辺はプリム法では問題にならないが、データとしては綺麗でないかも）
        # ここでは、同じ頂点間の辺が存在しない場合のみ追加するように修正します。
        
        # vertex1 -> vertex2 の辺を追加（重み付き）
        edge_exists_v1v2 = False
        for i, (neighbor, _) in enumerate(self._data[vertex1]):
            if neighbor == vertex2:
                self._data[vertex1][i] = (vertex2, weight) # 既に存在する場合は重みを更新
                edge_exists_v1v2 = True
                break
        if not edge_exists_v1v2:
            self._data[vertex1].append((vertex2, weight))

        # vertex2 -> vertex1 の辺を追加（重み付き）
        edge_exists_v2v1 = False
        for i, (neighbor, _) in enumerate(self._data[vertex2]):
            if neighbor == vertex1:
                self._data[vertex2][i] = (vertex1, weight) # 既に存在する場合は重みを更新
                edge_exists_v2v1 = True
                break
        if not edge_exists_v2v1:
            self._data[vertex2].append((vertex1, weight))
        
        return True

    def clear(self):
        # グラフを空にします。
        self._data = {}
        return True

    def get_shortest_path(self, start_vertex, end_vertex, heuristic):
        if start_vertex not in self._data or end_vertex not in self._data:
            print("ERROR: 開始頂点または終了頂点がグラフに存在しません。")
            return None, float('inf')

        if start_vertex == end_vertex:
            return [start_vertex], 0

        # g_costs: 開始ノードから各ノードまでの既知の最短コスト
        g_costs = {vertex: float('inf') for vertex in self._data}
        g_costs[start_vertex] = 0

        # f_costs: g_costs + ヒューリスティックコスト (推定合計コスト)
        f_costs = {vertex: float('inf') for vertex in self._data}
        f_costs[start_vertex] = heuristic(start_vertex, end_vertex)

        # came_from: 最短経路で各ノードの直前のノードを記録
        came_from = {}

        # open_set: 探索すべきノードの優先度キュー (f_cost, node)
        open_set = [(f_costs[start_vertex], start_vertex)]

        # すでに open_set に追加されたノードを追跡するためのセット
        # (heapq は要素の存在チェックが遅いため) - 必要に応じて。
        # 今回はf_costをキーにした辞書を使う方が効率的かも。
        # あるいは、heapqに重複要素が入ることを許容し、取り出し時にg_cost/f_costが古いかチェックする。
        # 後者のアプローチで実装します。

        # open_set に (f_cost, g_cost, node) のタプルを入れることで、
        # f_cost が同じ場合の tie-breaking に g_cost を使えるが、
        # 基本的な A* は (f_cost, node) で十分。ここではシンプルに (f_cost, node) を使用。
        # heapq.heappush(open_set, (f_costs[start_vertex], g_costs[start_vertex], start_vertex)) # if using (f, g, node)

        while open_set:
            # open_set から最も f_cost が低いノードを取り出す
            current_f_cost, current_vertex = heapq.heappop(open_set)
            # current_f_cost, current_g_cost, current_vertex = heapq.heappop(open_set) # if using (f, g, node)

            # 取り出したノードの f_cost が、記録されている f_costs[current_vertex] より大きい場合、
            # それは古い情報なので無視して次のノードに進む
            # (heapq は要素を削除できないため、より低い f_cost で同じノードが後から追加される可能性がある)
            if current_f_cost > f_costs[current_vertex]:
                 continue

            # 目標ノードに到達した場合、経路を再構築して返す
            if current_vertex == end_vertex:
                return self._reconstruct_path(came_from, end_vertex), g_costs[end_vertex]

            # 現在のノードの隣接ノードを調べる
            neighbors = self.get_neighbors(current_vertex)
            if neighbors is None: # 孤立したノードの場合など
                continue

            for neighbor, weight in neighbors:
                # 現在のノードを経由した場合の隣接ノードへの新しい g_cost
                tentative_g_cost = g_costs[current_vertex] + weight

                # 新しい g_cost が、現在記録されている隣接ノードへの g_cost よりも小さい場合
                if tentative_g_cost < g_costs[neighbor]:
                    # 経路情報を更新
                    came_from[neighbor] = current_vertex
                    g_costs[neighbor] = tentative_g_cost
                    f_costs[neighbor] = g_costs[neighbor] + heuristic(neighbor, end_vertex)
                    
                    # 隣接ノードを open_set に追加（または優先度を更新）
                    # heapq は優先度更新を直接サポートしないため、新しいエントリを追加
                    # 古いエントリは取り出し時のチェックでスキップされる
                    heapq.heappush(open_set, (f_costs[neighbor], neighbor))
                    # heapq.heappush(open_set, (f_costs[neighbor], g_costs[neighbor], neighbor)) # if using (f, g, node)

        # open_set が空になっても目標ノードに到達しなかった場合、経路は存在しない
        return None, float('inf')

    def _reconstruct_path(self, came_from, current_vertex):
        path = []
        while current_vertex in came_from:
            path.append(current_vertex)
            current_vertex = came_from[current_vertex]
        path.append(current_vertex) # 開始ノードを追加
        path.reverse() # 経路を逆順にする (開始 -> 目標)
        return path

# ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
# 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
def dummy_heuristic(u, v):
    # u と v の間に何らかの推定距離を計算する関数
    # ここではダミーとして常に0を返す
    return 0

def main():
    print("A-start TEST -----> start")

    graph_data = GraphData()

    graph_data.clear()
    inputList = [('A', 'B', 4), ('B', 'C', 3), ('B', 'D', 2), ('D', 'A', 1), ('A', 'C', 2), ('B', 'D', 2)]
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    input = ('A', 'B')
    shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
    print(f"経路{input[0]}-{input[1]} の最短経路は {shortest_path[0]} (重み: {shortest_path[1]})")

    graph_data.clear()
    inputList = [('A', 'B', 4), ('C', 'D', 4), ('E', 'F', 1), ('F', 'G', 1)]
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    input = ('A', 'B')
    shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
    print(f"経路{input[0]}-{input[1]} の最短経路は {shortest_path[0]} (重み: {shortest_path[1]})")

    graph_data.clear()
    inputList = [('A', 'B', 4), ('B', 'C', 3), ('D', 'E', 5)]
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    input = ('A', 'D')
    shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
    print(f"経路{input[0]}-{input[1]} の最短経路は {shortest_path[0]} (重み: {shortest_path[1]})")

    graph_data.clear()
    inputList = []
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    input = ('A', 'B')
    shortest_path = graph_data.get_shortest_path(input[0], input[1], dummy_heuristic)
    print(f"経路{input[0]}-{input[1]} の最短経路は {shortest_path[0]} (重み: {shortest_path[1]})")

    print("\nA-start TEST <----- end")

if __name__ == "__main__":
    main()