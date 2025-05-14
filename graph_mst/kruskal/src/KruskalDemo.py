# Python
# グラフの最小全域木: クラスカル法 (Kruskal)

class DSU:
    def __init__(self, vertices):
        # 各頂点の親を格納します。最初は各頂点自身が親です。
        self.parent = {v: v for v in vertices}
        # ランク（木の高さまたはサイズ）を格納し、union操作を最適化します。
        self.rank = {v: 0 for v in vertices}

    # 頂点 i が属する集合の代表元（根）を見つけます。
    # パス圧縮により効率化されます。
    def find(self, i):
        if self.parent[i] == i:
            return i
        # パス圧縮: 探索中に encountered したノードを根に直接つなぎ直します。
        self.parent[i] = self.find(self.parent[i])
        return self.parent[i]

    # 頂点 i と 頂点 j を含む二つの集合を結合します。
    # ランクによるunionにより効率化されます。
    def union(self, i, j):
        root_i = self.find(i)
        root_j = self.find(j)

        # 根が異なる場合のみ結合します（同じ集合に属している場合は何もしません）。
        if root_i != root_j:
            # ランクが小さい方の木を、ランクが大きい方の木の根に付けます。
            if self.rank[root_i] < self.rank[root_j]:
                self.parent[root_i] = root_j
            elif self.rank[root_i] > self.rank[root_j]:
                self.parent[root_j] = root_i
            else:
                # ランクが同じ場合はどちらかを親とし、その親のランクを増やします。
                self.parent[root_j] = root_i
                self.rank[root_i] += 1
            return True # 集合が結合された
        return False # 既に同じ集合に属していた


# 重みを扱えるように改変された GraphData クラス
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

    def get_mst(self):
        # 1. 全ての辺を取得し、重みでソートします。
        # get_edges() は (u, v, weight) のリストを返します。
        edges = self.get_edges()
        # 重み (タプルの3番目の要素) をキーとして辺をソート
        sorted_edges = sorted(edges, key=lambda item: item[2])

        # 2. Union-Findデータ構造を初期化します。
        # 各頂点が自身の集合に属するようにします。
        vertices = self.get_vertices()
        dsu = DSU(vertices)

        # 3. MSTを構築します。
        # 結果として得られるMSTの辺を格納するリスト
        mst_edges = []
        # MSTに追加された辺の数 (頂点数-1 になればMSTが完成)
        edges_count = 0

        # ソートされた辺を順番に調べます。
        for u, v, weight in sorted_edges:
            # 辺 (u, v) の両端点が属する集合の代表元（根）を見つけます。
            root_u = dsu.find(u)
            root_v = dsu.find(v)

            # 両端点が異なる集合に属する場合、その辺をMSTに追加してもサイクルは形成されません。
            if root_u != root_v:
                # 辺をMSTに追加します。
                mst_edges.append((u, v, weight))
                # 辺を追加したので、両端点の集合を結合します。
                dsu.union(u, v)
                # MSTに追加した辺の数を増やします。
                edges_count += 1

                # 頂点数から1を引いた数の辺がMSTに追加されたら終了です。
                # これはグラフが連結である場合に限り、完全なMSTが得られたことを意味します。
                # 連結でない場合は、全ての辺を調べ終えるまで続行し、最小全域森を得ます。
                if edges_count == len(vertices) - 1:
                    break

        # MST (または最小全域森) の辺のリストを返します。
        return mst_edges

def main():
    print("Kruskal TEST -----> start")
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

    print("\nKruskal TEST <----- end")

if __name__ == "__main__":
    main()