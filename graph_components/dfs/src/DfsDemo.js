// JavaScript
// グラフの連結成分: DFS

class GraphData {
  constructor() {
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みの配列です。
    // 例: { 'A': [['B', 1], ['C', 4]], 'B': [['A', 1], ['C', 2], ['D', 5]], ... }
    this._data = {};
  }

  get() {
    // グラフの内部データを取得します。
    return this._data;
  }

  getVertices() {
    // グラフの全頂点をリストとして返します。
    return Object.keys(this._data);
  }
  
  getEdges() {
    // グラフの全辺をリストとして返します。
    // 無向グラフの場合、[u, v, weight] の形式で返します。
    // 重複を避けるためにセットを使用します。
    const edges = new Set();
    for (const vertex in this._data) {
      for (const [neighbor, weight] of this._data[vertex]) {
        // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
        const edge = [vertex, neighbor].sort();
        edges.add(JSON.stringify([edge[0], edge[1], weight])); // JSON文字列化してセットに格納
      }
    }
    // JSONパースして配列として返す
    return Array.from(edges).map(edge => JSON.parse(edge));
  }
  
  getNeighbors(vertex) {
    // 指定された頂点の隣接ノードと辺の重みのリストを返します。
    // 形式: [[隣接頂点, 重み], ...]
    if (vertex in this._data) {
      return this._data[vertex];
    } else {
      return null; // 頂点が存在しない場合はNullを返す
    }
  }

  getEdgeWeight(vertex1, vertex2) {
    // 指定された2つの頂点間の辺の重みを返します。
    // 辺が存在しない場合はNullを返します。
    if (vertex1 in this._data && vertex2 in this._data) {
      for (const [neighbor, weight] of this._data[vertex1]) {
        if (neighbor === vertex2) {
          return weight;
        }
      }
    }
    return null; // 辺が存在しない場合
  }

  getVertice(vertex) {
    // 頂点がグラフに存在するか確認する
    if (vertex in this._data) {
      // 存在する場合は、その頂点の隣接リスト（関連する値）を返す
      return this._data[vertex];
    } else {
      // 存在しない場合はメッセージを表示し、Nullを返す
      console.log(`ERROR: ${vertex}は範囲外です`);
      return null;
    }
  }

  getEdge(vertex1, vertex2) {
    // 指定された2つの頂点間に辺が存在するかを確認する
    // 両方の頂点がグラフに存在する必要がある
    if (vertex1 in this._data && vertex2 in this._data) {
      // vertex1の隣接リストにvertex2が含まれているかを確認
      // 無向グラフなので、片方を確認すれば十分
      return this._data[vertex1].some(([neighbor]) => neighbor === vertex2);
    } else {
      // どちらかの頂点が存在しない場合は辺も存在しない
      return false;
    }
  }

  addVertex(vertex) {
    // 新しい頂点をグラフに追加します。
    if (!(vertex in this._data)) {
      this._data[vertex] = [];
      return true;
    }
    // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
    return true;
  }
  
  addEdge(vertex1, vertex2, weight) {
    // 両頂点間に辺を追加します。重みを指定します。
    // 頂点がグラフに存在しない場合は追加します。
    if (!(vertex1 in this._data)) {
      this.addVertex(vertex1);
    }
    if (!(vertex2 in this._data)) {
      this.addVertex(vertex2);
    }
    
    // 両方向に辺を追加する（無向グラフ）
    
    // vertex1 -> vertex2 の辺を追加（重み付き）
    let edgeExistsV1V2 = false;
    for (let i = 0; i < this._data[vertex1].length; i++) {
      if (this._data[vertex1][i][0] === vertex2) {
        this._data[vertex1][i] = [vertex2, weight]; // 既に存在する場合は重みを更新
        edgeExistsV1V2 = true;
        break;
      }
    }
    if (!edgeExistsV1V2) {
      this._data[vertex1].push([vertex2, weight]);
    }

    // vertex2 -> vertex1 の辺を追加（重み付き）
    let edgeExistsV2V1 = false;
    for (let i = 0; i < this._data[vertex2].length; i++) {
      if (this._data[vertex2][i][0] === vertex1) {
        this._data[vertex2][i] = [vertex1, weight]; // 既に存在する場合は重みを更新
        edgeExistsV2V1 = true;
        break;
      }
    }
    if (!edgeExistsV2V1) {
      this._data[vertex2].push([vertex1, weight]);
    }
    
    return true;
  }
  
  removeVertex(vertex) {
    // 頂点とそれに関連する辺を削除します。
    if (vertex in this._data) {
      // この頂点への参照を他の頂点の隣接リストから削除する
      for (const v in this._data) {
        this._data[v] = this._data[v].filter(([neighbor]) => neighbor !== vertex);
      }
      // 頂点自体を削除する
      delete this._data[vertex];
      return true;
    } else {
      console.log(`ERROR: ${vertex} は範囲外です`);
      return false;
    }
  }

  removeEdge(vertex1, vertex2) {
    // 両頂点間の辺を削除します。
    if (vertex1 in this._data && vertex2 in this._data) {
      let removed = false;
      // vertex1 から vertex2 への辺を削除
      const originalLenV1 = this._data[vertex1].length;
      this._data[vertex1] = this._data[vertex1].filter(([neighbor]) => neighbor !== vertex2);
      if (this._data[vertex1].length < originalLenV1) {
        removed = true;
      }

      // vertex2 から vertex1 への辺を削除
      const originalLenV2 = this._data[vertex2].length;
      this._data[vertex2] = this._data[vertex2].filter(([neighbor]) => neighbor !== vertex1);
      if (this._data[vertex2].length < originalLenV2) {
        removed = true;
      }
        
      return removed; // 少なくとも片方向が削除されたか
    } else {
      console.log(`ERROR: ${vertex1} または ${vertex2} は範囲外です`);
      return false;
    }
  }

  isEmpty() {
    // グラフが空かどうか
    return Object.keys(this._data).length === 0;
  }
  
  size() {
    // グラフの頂点数を返す
    return Object.keys(this._data).length;
  }
  
  clear() {
    // グラフを空にする
    this._data = {};
    return true;
  }

  _dfs(vertex, visited, currentComponent) {
    // 現在の頂点を訪問済みにマークし、現在の成分に追加
    visited.add(vertex);
    currentComponent.push(vertex);

    // 隣接する頂点を探索
    // this._data[vertex] || [] は、vertexが存在しない場合でも
    // エラーにならず空配列を返す安全な方法
    for (const [neighborVertex, _] of this._data[vertex] || []) {
      // まだ訪問していない隣接頂点に対して再帰的にDFSを呼び出す
      if (!visited.has(neighborVertex)) {
        this._dfs(neighborVertex, visited, currentComponent);
      }
    }
  }

  getConnectedComponents() {
    const visited = new Set(); // 訪問済み頂点を記録するセット
    const connectedComponents = []; // 連結成分を格納する配列

    // グラフの全頂点を順にチェック
    for (const vertex of this.getVertices()) {
      // まだ訪問していない頂点からDFSを開始
      if (!visited.has(vertex)) {
        const currentComponent = []; // 現在の連結成分を格納する配列
        // DFSヘルパー関数を呼び出し、現在の連結成分を探索
        this._dfs(vertex, visited, currentComponent);
        // 探索で見つかった連結成分を結果リストに追加
        connectedComponents.push(currentComponent);
      }
    }

    return connectedComponents;
  }
}

function main() {
  console.log("Dfs TEST -----> start");

  console.log("\nnew");
  const graphData = new GraphData();
  console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

  console.log("\nadd_edge");
  graphData.clear();
  const inputList1 = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]];
  for (const input of inputList1) {
    console.log(`  入力値: ${JSON.stringify(input)}`);
    const output = graphData.addEdge(input[0], input[1], input[2]);
    console.log(`  出力値: ${output}`);
  }
  console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
  console.log("\nget_connected_components");
  const output1 = graphData.getConnectedComponents();
  console.log(`  連結成分: ${JSON.stringify(output1)}`);

  console.log("\nadd_edge");
  graphData.clear();
  const inputList2 = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]];
  for (const input of inputList2) {
    console.log(`  入力値: ${JSON.stringify(input)}`);
    const output = graphData.addEdge(input[0], input[1], input[2]);
    console.log(`  出力値: ${output}`);
  }
  console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
  console.log("\nget_connected_components");
  const output2 = graphData.getConnectedComponents();
  console.log(`  連結成分: ${JSON.stringify(output2)}`);

  console.log("\nadd_edge");
  graphData.clear();        
  const inputList3 = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]];
  for (const input of inputList3) {
    console.log(`  入力値: ${JSON.stringify(input)}`);
    const output = graphData.addEdge(input[0], input[1], input[2]);
    console.log(`  出力値: ${output}`);
  }
  console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
  console.log("\nget_connected_components");
  const output3 = graphData.getConnectedComponents();
  console.log(`  連結成分: ${JSON.stringify(output3)}`);

  console.log("\nadd_edge");
  graphData.clear();
  const inputList4 = [];
  for (const input of inputList4) {
    console.log(`  入力値: ${JSON.stringify(input)}`);
    const output = graphData.addEdge(input[0], input[1], input[2]);
    console.log(`  出力値: ${output}`);
  }
  console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
  console.log("\nget_connected_components");
  const output4 = graphData.getConnectedComponents();
  console.log(`  連結成分: ${JSON.stringify(output4)}`);

  console.log("Dfs TEST <----- end");
}

main();