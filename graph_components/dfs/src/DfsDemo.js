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