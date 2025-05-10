# Python
# グラフの連結成分: Union-Find

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

    def get_edge_weight(self, vertex1, vertex2):
        # 指定された2つの頂点間の辺の重みを返します。
        # 辺が存在しない場合はNoneを返します。
        if vertex1 in self._data and vertex2 in self._data:
            for neighbor, weight in self._data[vertex1]:
                if neighbor == vertex2:
                    return weight
        return None # 辺が存在しない場合

    def get_vertice(self, vertex):
        # 頂点がグラフに存在するか確認する
        if vertex in self._data:
            # 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            return self._data[vertex]
        else:
            # 存在しない場合はメッセージを表示し、Noneを返す
            print(f"ERROR: {vertex}は範囲外です")
            return None

    def get_edge(self, vertex1, vertex2):
        # 指定された2つの頂点間に辺が存在するかを確認する
        # 両方の頂点がグラフに存在する必要がある
        if vertex1 in self._data and vertex2 in self._data:
            # vertex1の隣接リストにvertex2が含まれているかを確認
            # 無向グラフなので、片方を確認すれば十分
            return vertex2 in self._data[vertex1]
        else:
            # どちらかの頂点が存在しない場合は辺も存在しない
            return False

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
            print(f"ERROR: {vertex} は範囲外です")
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
            print(f"ERROR: {vertex1} または {vertex2} は範囲外です")
            return False

    def is_empty(self):
        # グラフが空かどうか
        return len(self._data) == 0
    
    def size(self):
        # グラフの頂点数を返す
        return len(self._data)
    
    def clear(self):
        # グラフを空にする
        self._data = {}
        return True

    def get_connected_components(self):
        if not self._data:
            return [] # 空のグラフの場合は空リストを返す

        # Union-Findのためのデータ構造を初期化
        # parent[i] は要素 i の親を示す
        # size[i] は要素 i を根とする集合のサイズを示す (Union by Size用)
        parent = {}
        size = {}

        # 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
        vertices = self.get_vertices()
        for vertex in vertices:
            parent[vertex] = vertex
            size[vertex] = 1

        def find(v):
            # 経路圧縮 (Path Compression) を伴う Find 操作
            # vの親がv自身でなければ、根を探しにいく
            if parent[v] != v:
                # 見つけた根をvの直接の親として記録 (経路圧縮)
                parent[v] = find(parent[v])
            return parent[v] # 最終的に根を返す

        def union(u, v):
            # Union by Size を伴う Union 操作
            root_u = find(u)
            root_v = find(v)

            # 根が同じ場合は、すでに同じ集合に属しているので何もしない
            if root_u != root_v:
                # より小さいサイズの木を大きいサイズの木に結合する
                if size[root_u] < size[root_v]:
                    parent[root_u] = root_v
                    size[root_v] += size[root_u]
                else:
                    parent[root_v] = root_u
                    size[root_u] += size[root_v]
                return True # 結合が行われた
            return False # 結合は行われなかった

        # グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
        # get_edges()は (u, v, weight) のタプルを返すため、重みを無視してunionを呼び出す
        for edge in self.get_edges(): # 修正: get_edgesの出力形式 (u, v, weight) に合わせる
            u, v, _ = edge  # 修正: タプルから u と v だけを取り出し、重み (_) は無視する
            union(u, v)

        # 連結成分をグループ化する
        # 根をキーとして、その根に属する頂点のリストを値とする辞書を作成
        components = {}
        for vertex in vertices:
            root = find(vertex) # 各頂点の最終的な根を見つける
            if root not in components:
                components[root] = []
            components[root].append(vertex)

        # 連結成分のリスト（値の部分）を返す
        return list(components.values())
    
def main():
    print("UnionFind TEST -----> start")

    print("\nnew")
    graph_data = GraphData()
    print(f"  現在のデータ: {graph_data.get()}")

    print("\nadd_edge")
    graph_data.clear()
    inputList = [('A', 'B', 4), ('B', 'C', 3), ('B', 'D', 2), ('D', 'A', 1), ('A', 'C', 2), ('B', 'D', 2)]
    for input in inputList:
        print(f"  入力値: {input}")
        output = graph_data.add_edge(*input)
        print(f"  出力値: {output}")
    print(f"  現在のデータ: {graph_data.get()}")
    print("\nget_connected_components")
    output = graph_data.get_connected_components()
    print(f"  連結成分: {output}")

    print("\nadd_edge")
    graph_data.clear()
    inputList = [('A', 'B', 4), ('C', 'D', 4), ('E', 'F', 1), ('F', 'G', 1)]
    for input in inputList:
        print(f"  入力値: {input}")
        output = graph_data.add_edge(*input)
        print(f"  出力値: {output}")
    print(f"  現在のデータ: {graph_data.get()}")
    print("\nget_connected_components")
    output = graph_data.get_connected_components()
    print(f"  連結成分: {output}")

    print("\nadd_edge")
    graph_data.clear()        
    inputList = [('A', 'B', 4), ('B', 'C', 3), ('D', 'E', 5)]
    for input in inputList:
        print(f"  入力値: {input}")
        output = graph_data.add_edge(*input)
        print(f"  出力値: {output}")
    print(f"  現在のデータ: {graph_data.get()}")
    print("\nget_connected_components")
    output = graph_data.get_connected_components()
    print(f"  連結成分: {output}")

    print("\nadd_edge")
    graph_data.clear()
    inputList = []
    for input in inputList:
        print(f"  入力値: {input}")
        output = graph_data.add_edge(*input)
        print(f"  出力値: {output}")
    print(f"  現在のデータ: {graph_data.get()}")
    print("\nget_connected_components")
    output = graph_data.get_connected_components()
    print(f"  連結成分: {output}")

    print("\nUnionFind TEST <----- end")

if __name__ == "__main__":
    main()