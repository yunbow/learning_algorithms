# Python
# グラフの最短経路: ダイクストラ法 (dijkstra)
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
            print(f"ERROR: 開始頂点 '{start_vertex}' または 終了頂点 '{end_vertex}' がグラフに存在しません。")
            return (None, float('inf'))

        # 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
        distances = {vertex: float('inf') for vertex in self._data}
        distances[start_vertex] = 0

        # 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
        predecessors = {vertex: None for vertex in self._data}

        # 優先度付きキュー: (距離, 頂点) のタプルを格納し、距離が小さい順に取り出す
        # heapq は最小ヒープとして機能する
        priority_queue = [(0, start_vertex)] # (開始頂点への距離, 開始頂点)

        while priority_queue:
            # 優先度付きキューから最も距離の小さい頂点を取り出す
            current_distance, current_vertex = heapq.heappop(priority_queue)

            # 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
            # より短い経路が既に見つかっているためスキップ
            if current_distance > distances[current_vertex]:
                continue

            # 終了頂点に到達したら探索終了
            if current_vertex == end_vertex:
                break # 最短経路が見つかった

            # 現在の頂点から到達可能な隣接頂点を探索
            # self.get_neighbors(current_vertex) は [(neighbor, weight), ...] のリストを返す
            for neighbor, weight in self.get_neighbors(current_vertex):
                distance_through_current = distances[current_vertex] + weight

                # より短い経路が見つかった場合
                if distance_through_current < distances[neighbor]:
                    distances[neighbor] = distance_through_current
                    predecessors[neighbor] = current_vertex
                    # 優先度付きキューに隣接頂点を追加または更新
                    # ダイクストラ法では heuristic は使用しない (または h=0)
                    heapq.heappush(priority_queue, (distance_through_current, neighbor))

        # 終了頂点への最短距離が無限大のままなら、到達不可能
        if distances[end_vertex] == float('inf'):
            print(f"INFO: 開始頂点 '{start_vertex}' から 終了頂点 '{end_vertex}' への経路は存在しません。")
            return (None, float('inf'))

        # 最短経路を再構築
        path = []
        current = end_vertex
        while current is not None:
            path.append(current)
            current = predecessors[current]
        path.reverse() # 経路は逆順に構築されたので反転

        # 開始ノードから開始されていることを確認
        if path[0] != start_vertex:
             # これが発生するのは、開始頂点が存在しないか、到達不能な場合だが、
             # 前段のチェックで大部分はカバーされているはず。
             # ここに来る場合は、特殊なケース（例えば孤立した開始点と終了点）が考えられる。
             return (None, float('inf'))

        return (path, distances[end_vertex])

# ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
# 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
def dummy_heuristic(u, v):
    # u と v の間に何らかの推定距離を計算する関数
    # ここではダミーとして常に0を返す
    return 0

def main():
    print("Dijkstra -----> start")

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

    print("\nDijkstra <----- end")

if __name__ == "__main__":
    main()