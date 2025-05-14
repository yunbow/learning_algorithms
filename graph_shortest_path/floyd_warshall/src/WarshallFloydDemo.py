# Python
# グラフの最短経路: ワーシャルフロイド法 (Warshall Floyd)

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
        vertices = self.get_vertices()
        num_vertices = len(vertices)
        if num_vertices == 0:
             return (None, float('inf'))

        # 頂点名をインデックスにマッピング
        vertex_to_index = {vertex: index for index, vertex in enumerate(vertices)}
        index_to_vertex = {index: vertex for index, vertex in enumerate(vertices)}

        # 開始・終了頂点が存在するか確認
        if start_vertex not in vertex_to_index or end_vertex not in vertex_to_index:
            print(f"ERROR: {start_vertex} または {end_vertex} がグラフに存在しません。")
            return (None, float('inf'))

        start_index = vertex_to_index[start_vertex]
        end_index = vertex_to_index[end_vertex]

        # 距離行列 (dist) と経路復元用行列 (next_node) を初期化
        # 無限大には sys.float_info.max または float('inf') を使用
        INF = float('inf') # sys.float_info.max は非常に大きいが有限の値
        dist = [[INF] * num_vertices for _ in range(num_vertices)]
        next_node = [[None] * num_vertices for _ in range(num_vertices)]

        # 初期距離と経路復元情報を設定
        for i in range(num_vertices):
            dist[i][i] = 0 # 自分自身への距離は0
            for neighbor, weight in self.get_neighbors(vertices[i]) or []: # Noneの場合も考慮
                 j = vertex_to_index[neighbor]
                 dist[i][j] = weight
                 next_node[i][j] = j # iからjへの直接辺の場合、iの次はj

        # ワーシャル-フロイド法の本体
        # k: 中継点として使用する頂点のインデックス
        for k in range(num_vertices):
            # i: 開始頂点のインデックス
            for i in range(num_vertices):
                # j: 終了頂点のインデックス
                for j in range(num_vertices):
                    # i -> k -> j の経路が i -> j の現在の経路より短い場合
                    # dist[i][k] や dist[k][j] がINFの場合は加算でINFになるため、
                    # 比較条件 `dist[i][k] + dist[k][j] < dist[i][j]` だけで十分ですが、
                    # 浮動小数点演算の誤差を避けるためにINFチェックを入れることもあります。
                    if dist[i][k] != INF and dist[k][j] != INF and dist[i][k] + dist[k][j] < dist[i][j]:
                        dist[i][j] = dist[i][k] + dist[k][j]
                        next_node[i][j] = next_node[i][k] # iからjへの最短経路で、iの次の頂点はiからkへの最短経路でのiの次の頂点

        # 負閉路のチェック (オプション)
        # for i in range(num_vertices):
        #     if dist[i][i] < 0:
        #         print("負閉路が存在します。最短経路は定義できません。")
        #         # 負閉路が存在する場合の戻り値をどうするかは仕様によります。
        #         # ここでは簡単な例なので、チェックはコメントアウトします。

        # 指定された開始・終了頂点間の最短経路と重みを取得
        shortest_distance = dist[start_index][end_index]

        # 経路が存在しない場合 (距離がINF)
        if shortest_distance == INF:
            return (None, INF)

        # 経路を復元
        path = []
        u = start_index
        # 開始と終了が同じ場合は経路は開始頂点のみ
        if u == end_index:
             path = [start_vertex]
        else:
            # next_nodeを使って経路をたどる
            while u is not None and u != end_index:
                path.append(index_to_vertex[u])
                u = next_node[u][end_index]
                # 無限ループ防止のための簡易チェック (到達不能なのにnext_nodeがNoneでない場合など)
                if u is not None and index_to_vertex[u] == path[-1]:
                     # 同じ頂点に戻ってきたなど、異常な経路復元を防ぐ
                     print(f"WARNING: 経路復元中に異常を検出しました（{index_to_vertex[u]}でループ？）。")
                     path = None
                     shortest_distance = INF
                     break
            # 最後のノード (end_vertex) を追加
            if path is not None:
                 path.append(end_vertex)

        return (path, shortest_distance)

# ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
# 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
def dummy_heuristic(u, v):
    # u と v の間に何らかの推定距離を計算する関数
    # ここではダミーとして常に0を返す
    return 0

def main():
    print("WarshallFloyd -----> start")

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

    print("\nWarshallFloyd <----- end")

if __name__ == "__main__":
    main()