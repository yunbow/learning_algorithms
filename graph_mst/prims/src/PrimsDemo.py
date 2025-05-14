# Python
# グラフの最小全域木: プリム法 (Prim)
import heapq

class GraphData:
    def __init__(self):
        # 隣接リストとしてグラフデータを格納します。
        # キーは頂点、値はその頂点に隣接する頂点とその辺の重みのタプルのリストです。
        # 例: { 'A': [('B', 1), ('C', 4)], 'B': [('A', 1), ('C', 2)], ... }
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

    def get_edge_weight(self, vertex1, vertex2):
        # 指定された2つの頂点間の辺の重みを返します。
        # 辺が存在しない場合はNoneを返します。
        if vertex1 in self._data and vertex2 in self._data:
            for neighbor, weight in self._data[vertex1]:
                if neighbor == vertex2:
                    return weight
        return None # 辺が存在しない場合

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
    
    def is_empty(self):
        # グラフが空かどうかを返します。
        return len(self._data) == 0
        
    def clear(self):
        # グラフを空にします。
        self._data = {}
        return True

    def get_mst(self, start_vertex=None):
        vertices = self.get_vertices()
        if not vertices:
            return [] # グラフが空

        if start_vertex is None:
            start_vertex = vertices[0]
        elif start_vertex not in self._data:
             print(f"ERROR: 開始頂点 {start_vertex} はグラフに存在しません。")
             return None

        # MSTに含まれる頂点のセット
        in_mst = set()
        # 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
        # heapqは最小ヒープなので、重みが小さい辺から取り出される
        min_heap = []
        # MSTを構成する辺のリスト
        mst_edges = []
        # 各頂点への最小コスト（MSTに追加する際の辺の重み）と、その遷移元の頂点を記録
        min_cost = {v: float('inf') for v in vertices}
        parent = {v: None for v in vertices}

        # 開始頂点の処理
        min_cost[start_vertex] = 0
        heapq.heappush(min_heap, (0, start_vertex, None)) # (コスト, 現在の頂点, 遷移元の頂点)

        while min_heap:
            # 最小コストの辺を持つ頂点を取り出す
            cost, current_vertex, from_vertex = heapq.heappop(min_heap)

            # 既にMSTに含まれている頂点であればスキップ
            if current_vertex in in_mst:
                continue

            # 現在の頂点をMSTに追加
            in_mst.add(current_vertex)

            # MSTに追加された辺を記録 (開始頂点以外)
            if from_vertex is not None:
                # from_vertex から current_vertex への辺の重みを取得
                weight = self.get_edge_weight(from_vertex, current_vertex)
                if weight is not None:
                    mst_edges.append(tuple(sorted((from_vertex, current_vertex))) + (weight,)) # 辺を正規化して追加

            # 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
            neighbors_with_weight = self.get_neighbors(current_vertex)
            if neighbors_with_weight: # 隣接する頂点がある場合
                for neighbor, weight in neighbors_with_weight:
                    # 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
                    if neighbor not in in_mst and weight < min_cost[neighbor]:
                        min_cost[neighbor] = weight
                        parent[neighbor] = current_vertex
                        heapq.heappush(min_heap, (weight, neighbor, current_vertex))

        # グラフが非連結で、MSTに含まれる頂点が全体の頂点数より少ない場合
        # プリム法は開始頂点を含む連結成分のMSTのみを求めます。
        # この実装では、開始頂点を含む連結成分のMST辺のみを返します。
        # 必要であれば、非連結グラフの森を求めるように拡張することも可能です。

        # 全ての頂点がMSTに含まれたか確認（連結グラフの場合）
        # if len(in_mst) != len(vertices):
        #     print("Warning: グラフは非連結の可能性があります。開始頂点を含む連結成分のMSTを返します。")

        return mst_edges

def main():
    print("Prims TEST -----> start")
    graph_data = GraphData()

    graph_data.clear()
    inputList = [('A', 'B', 4), ('B', 'C', 3), ('B', 'D', 2), ('D', 'A', 1), ('A', 'C', 2), ('B', 'D', 2)]
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    outputMst = graph_data.get_mst()
    for edge in outputMst:
        print(f"Edge: {edge[0]} - {edge[1]}, Weight: {edge[2]}")
    total_weight = sum(edge[2] for edge in outputMst)
    print(f"最小全域木の合計重み: {total_weight}")

    graph_data.clear()
    inputList = [('A', 'B', 4), ('C', 'D', 4), ('E', 'F', 1), ('F', 'G', 1)]
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    outputMst = graph_data.get_mst()
    for edge in outputMst:
        print(f"Edge: {edge[0]} - {edge[1]}, Weight: {edge[2]}")
    total_weight = sum(edge[2] for edge in outputMst)
    print(f"最小全域木の合計重み: {total_weight}")

    graph_data.clear()
    inputList = [('A', 'B', 4), ('B', 'C', 3), ('D', 'E', 5)]
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    outputMst = graph_data.get_mst()
    for edge in outputMst:
        print(f"Edge: {edge[0]} - {edge[1]}, Weight: {edge[2]}")
    total_weight = sum(edge[2] for edge in outputMst)
    print(f"最小全域木の合計重み: {total_weight}")

    graph_data.clear()
    inputList = []
    for input in inputList:
        graph_data.add_edge(*input)
    print("\nグラフの頂点:", graph_data.get_vertices())
    print("グラフの辺 (重み付き):", graph_data.get_edges())
    outputMst = graph_data.get_mst()
    for edge in outputMst:
        print(f"Edge: {edge[0]} - {edge[1]}, Weight: {edge[2]}")
    total_weight = sum(edge[2] for edge in outputMst)
    print(f"最小全域木の合計重み: {total_weight}")

    print("\nPrims TEST <----- end")

if __name__ == "__main__":
    main()