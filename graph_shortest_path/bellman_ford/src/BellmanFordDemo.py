# Python
# グラフの最短経路: ベルマンフォード法 (Bellman Ford)
import heapq # これはA*やダイクストラ法で使われるため、ベルマン・フォード法では直接使用しませんが、元のコードに合わせてインポートは維持します。
import sys

class GraphData:
    def __init__(self):
        # 隣接ノードとその辺の重みを格納します。
        # キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
        # 例: { 'A': [('B', 1), ('C', 4)], 'B': [('A', 1), ('C', 2, 'D', 5)], ... }
        # add_edge で双方向に追加されるため、実質無向グラフを表しますが、
        # _data の形式は有向辺のリストとして扱えます。
        self._data = {}

    def get(self):
        # グラフの内部データを取得します。
        return self._data

    def get_vertices(self):
        # グラフの全頂点をリストとして返します。
        return list(self._data.keys())

    def get_edges(self):
        # グラフの全辺をリストとして返します。
        # ベルマン-フォード法で使用するため、内部データ (_data) から有向辺として抽出します。
        # 各辺は (出発頂点, 到着頂点, 重み) のタプルになります。
        edges = []
        for u in self._data:
            for v, weight in self._data[u]:
                edges.append((u, v, weight))
        return edges

    def add_vertex(self, vertex):
        # 新しい頂点をグラフに追加します。
        if vertex not in self._data:
            self._data[vertex] = []
            return True
        return True # 既に存在する場合は追加しないがTrueを返す

    def add_edge(self, vertex1, vertex2, weight):
        # 両頂点間に辺を追加します。重みを指定します。
        # 頂点がグラフに存在しない場合は追加します。
        self.add_vertex(vertex1)
        self.add_vertex(vertex2)

        # vertex1 -> vertex2 の辺を追加（重み付き）
        # 既に同じ頂点間の辺が存在する場合は重みを更新
        edge_updated_v1v2 = False
        for i, (neighbor, _) in enumerate(self._data[vertex1]):
            if neighbor == vertex2:
                self._data[vertex1][i] = (vertex2, weight)
                edge_updated_v1v2 = True
                break
        if not edge_updated_v1v2:
            self._data[vertex1].append((vertex2, weight))

        # vertex2 -> vertex1 の辺を追加（重み付き）
        # 既に同じ頂点間の辺が存在する場合は重みを更新
        edge_updated_v2v1 = False
        for i, (neighbor, _) in enumerate(self._data[vertex2]):
            if neighbor == vertex1:
                self._data[vertex2][i] = (vertex1, weight)
                edge_updated_v2v1 = True
                break
        if not edge_updated_v2v1:
            self._data[vertex2].append((vertex1, weight))

        return True


    def remove_vertex(self, vertex):
        # 頂点とそれに関連する辺を削除します。
        if vertex in self._data:
            # この頂点への参照を他の頂点の隣接リストから削除する
            for v in self._data:
                self._data[v] = [(neighbor, weight) for neighbor, weight in self._data[v] if neighbor != vertex]
            # 頂点自体を削除する
            del self._data[vertex]
            return True
        else:
            # print(f"ERROR: {vertex} は範囲外です") # エラーメッセージは控えめに
            return False

    def remove_edge(self, vertex1, vertex2):
        # 両頂点間の辺を削除します。
        if vertex1 in self._data and vertex2 in self._data:
            removed = False
            # vertex1 から vertex2 への辺を削除
            original_len_v1 = len(self._data[vertex1])
            self._data[vertex1] = [(neighbor, weight) for neighbor, weight in self._data[vertex1] if neighbor != vertex2]
            if len(self._data[vertex1]) < original_len_v1:
                removed = True

            # vertex2 から vertex1 への辺を削除
            original_len_v2 = len(self._data[vertex2])
            self._data[vertex2] = [(neighbor, weight) for neighbor, weight in self._data[vertex2] if neighbor != vertex1]
            if len(self._data[vertex2]) < original_len_v2:
                removed = True

            return removed # 少なくとも片方向が削除されたか

        else:
            # print(f"ERROR: {vertex1} または {vertex2} は範囲外です") # エラーメッセージは控えめに
            return False

    def is_empty(self):
        # グラフが空かどうかを返します。
        return len(self._data) == 0

    def size(self):
        # グラフの頂点数を返します。
        return len(self._data)

    def clear(self):
        # グラフを空にします。
        self._data = {}
        return True

    def get_shortest_path(self, start_vertex, end_vertex, heuristic):
        vertices = self.get_vertices()
        edges = self.get_edges() # 有向辺のリストを取得
        num_vertices = len(vertices)

        # 始点と終点の存在チェック
        if start_vertex not in vertices:
             print(f"エラー: 始点 '{start_vertex}' がグラフに存在しません。")
             return ([], float('inf'))
        if end_vertex not in vertices:
             print(f"エラー: 終点 '{end_vertex}' がグラフに存在しません。")
             return ([], float('inf'))

        # 始点と終点が同じ場合
        if start_vertex == end_vertex:
             return ([start_vertex], 0)

        # 距離と先行頂点の初期化
        # dist: 始点からの最短距離を格納
        # pred: 最短経路における各頂点の先行頂点を格納
        dist = {vertex: float('inf') for vertex in vertices}
        pred = {vertex: None for vertex in vertices}
        dist[start_vertex] = 0 # 始点自身の距離は0

        # |V| - 1 回の緩和ステップを実行
        # このループの後、負閉路が存在しない場合は全ての頂点への最短距離が確定している
        for _ in range(num_vertices - 1):
            # 緩和が一度も行われなかった場合にループを中断するためのフラグ
            relaxed_in_this_iteration = False
            for u, v, weight in edges:
                # dist[u] が無限大でない場合のみ緩和を試みる（到達不可能な頂点からの緩和は意味がない）
                if dist[u] != float('inf') and dist[u] + weight < dist[v]:
                    dist[v] = dist[u] + weight
                    pred[v] = u
                    relaxed_in_this_iteration = True
            # このイテレーションで緩和が行われなかった場合は、それ以上距離が更新されることはないのでループを抜ける
            if not relaxed_in_this_iteration:
                break

        # 負閉路の検出
        # もう一度全ての辺に対して緩和を試みる。
        # ここでさらに距離が更新される辺が存在する場合、その辺は負閉路の一部であるか、
        # 負閉路から到達可能な頂点への辺である。
        # 終点が負閉路から到達可能な場合、終点までの最短距離は無限小になるため定義できない。
        for u, v, weight in edges:
            if dist[u] != float('inf') and dist[u] + weight < dist[v]:
                # 負閉路が存在します。
                # 終点がこの負閉路から到達可能であれば、最短経路は存在しません。
                # 厳密には終点への到達可能性をチェックすべきですが、ベルマン・フォード法では負閉路自体の検出をもって最短経路定義不可とすることが一般的です。
                print("エラー: グラフに負閉路が存在します。最短経路は定義できません。")
                return (None, float('-inf')) # 負の無限大を返すことで、距離が無限小になることを示す

        # 最短経路の構築
        path = []
        current = end_vertex

        # 終点まで到達不可能かチェック (距離が初期値のままか)
        if dist[end_vertex] == float('inf'):
            return ([], float('inf')) # 到達不可能

        # 終点から先行頂点をたどって経路を逆順に構築
        while current is not None:
            path.append(current)
            # 始点に到達したらループを終了
            if current == start_vertex:
                 break
            # 次の頂点に進む
            current = pred[current]

        # 経路が始点から始まっていない場合 (通常は到達不可能な場合に含まれるはずだが、念のため)
        # pathリストの最後の要素がstart_vertexであることを確認
        if not path or path[-1] != start_vertex:
             # このケースは dist[end_vertex] == float('inf') で既に処理されているはずだが、念のため
             # または、負閉路検出後に到達不可能と判断されるケースもありうる
             return ([], float('inf'))

        path.reverse() # 経路を始点から終点の順にする

        return (path, dist[end_vertex])

# ヒューリスティック関数 (ベルマン-フォード法では使用しないが、元のコードに合わせた引数のために残す)
def dummy_heuristic(u, v):
    # u と v の間に何らかの推定距離を計算する関数
    # ここではベルマン-フォード法では使用しないため、常に0を返す
    return 0

def main():
    print("BellmanFord TEST -----> start")

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

    print("\nBellmanFord TEST <----- end")

if __name__ == "__main__":
    main()