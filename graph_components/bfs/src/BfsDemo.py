# Python
# グラフの連結成分: BFS
import collections

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
        """
        グラフの連結成分をBFSを使用して見つけます。

        Returns:
            list: 連結成分のリスト。各要素は連結成分を構成する頂点のリストです。
        """
        visited = set()  # 全体の訪問済み頂点を記録するセット
        all_components = [] # 見つかった連結成分のリスト

        # グラフのすべての頂点を取得
        vertices = self.get_vertices()

        # すべての頂点を順番にチェック
        for vertex in vertices:
            # もしその頂点がまだ訪問されていなければ、新しい連結成分の開始点
            if vertex not in visited:
                current_component = [] # 現在探索中の連結成分
                queue = collections.deque([vertex]) # BFS用のキュー
                visited.add(vertex) # 開始点を訪問済みにマーク
                current_component.append(vertex) # 開始点を現在の成分に追加

                # BFSを開始
                while queue:
                    u = queue.popleft() # キューから頂点を取り出す

                    # 取り出した頂点の隣接リストを取得 (重み情報を含む)
                    # get_vertice メソッドを get_neighbors に変更
                    neighbors_with_weight = self.get_neighbors(u)

                    # 頂点が存在し、隣接ノードがある場合
                    if neighbors_with_weight is not None:
                        # 隣接ノードだけを取り出してループ
                        for neighbor, weight in neighbors_with_weight: # タプルを展開して隣接頂点を取得
                            # 隣接する頂点がまだ訪問されていなければ
                            if neighbor not in visited: # 頂点そのものと比較
                                visited.add(neighbor) # 頂点そのものを訪問済みにマーク
                                queue.append(neighbor) # 頂点そのものをキューに追加
                                current_component.append(neighbor) # 頂点そのものを現在の成分に追加

                # BFSが終了したら、1つの連結成分が見つかった
                all_components.append(current_component)

        return all_components

def main():
    print("Bfs TEST -----> start")

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

    print("Bfs TEST <----- end")

if __name__ == "__main__":
    main()