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
        
    def get_neighbors(self, vertex):
        # 指定された頂点の隣接ノードと辺の重みのリストを返します。
        # 形式: [(隣接頂点, 重み), ...]
        if vertex in self._data:
            return self._data[vertex]
        else:
            return None # 頂点が存在しない場合はNoneを返す

    def get_vertice(self, vertex):
        # 頂点がグラフに存在するか確認する
        if vertex in self._data:
            # 存在する場合は、その頂点の隣接リスト（関連する値）を返す
            return self._data[vertex]
        else:
            # 存在しない場合はメッセージを表示し、Noneを返す
            print(f"ERROR: {vertex}は範囲外です")
            return None

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