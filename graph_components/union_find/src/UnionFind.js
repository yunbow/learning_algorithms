// JavaScript
// グラフの連結成分: Union-Find

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
        // setにはオブジェクトが直接入らないので文字列化して格納
        edges.add(JSON.stringify([edge[0], edge[1], weight]));
      }
    }
    // 文字列化したものを元に戻して返す
    return Array.from(edges).map(edge => JSON.parse(edge));
  }
  
  addVertex(vertex) {
    // 新しい頂点をグラフに追加します。
    if (!(vertex in this._data)) {
      this._data[vertex] = [];
      return true;
    }
    // 既に存在する場合は追加しないがtrueを返す（変更なしでも成功とみなす）
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
    // 既に同じ辺が存在する場合は重みを更新する
    
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
  
  isEmpty() {
    // グラフが空かどうか
    return Object.keys(this._data).length === 0;
  }
  
  clear() {
    // グラフを空にする
    this._data = {};
    return true;
  }

  getConnectedComponents() {
    if (Object.keys(this._data).length === 0) {
      return []; // 空のグラフの場合は空配列を返す
    }

    // Union-Findのためのデータ構造を初期化
    // parent[i] は要素 i の親を示す
    // size[i] は要素 i を根とする集合のサイズを示す (Union by Size用)
    const parent = {};
    const size = {};

    // 各頂点を初期状態では自分自身の集合に属させ、サイズを1とする
    const vertices = this.getVertices();
    for (const vertex of vertices) {
      parent[vertex] = vertex;
      size[vertex] = 1;
    }

    const find = (v) => {
      // 経路圧縮 (Path Compression) を伴う Find 操作
      // vの親がv自身でなければ、根を探しにいく
      if (parent[v] !== v) {
        // 見つけた根をvの直接の親として記録 (経路圧縮)
        parent[v] = find(parent[v]);
      }
      return parent[v]; // 最終的に根を返す
    };

    const union = (u, v) => {
      // Union by Size を伴う Union 操作
      const rootU = find(u);
      const rootV = find(v);

      // 根が同じ場合は、すでに同じ集合に属しているので何もしない
      if (rootU !== rootV) {
        // より小さいサイズの木を大きいサイズの木に結合する
        if (size[rootU] < size[rootV]) {
          parent[rootU] = rootV;
          size[rootV] += size[rootU];
        } else {
          parent[rootV] = rootU;
          size[rootU] += size[rootV];
        }
        return true; // 結合が行われた
      }
      return false; // 結合は行われなかった
    };

    // グラフの全ての辺に対してUnion操作を行い、連結成分をマージする
    for (const edge of this.getEdges()) {
      const [u, v, _] = edge; // 辺の両端の頂点を取得し、重みは無視する
      union(u, v);
    }

    // 連結成分をグループ化する
    // 根をキーとして、その根に属する頂点の配列を値とする辞書を作成
    const components = {};
    for (const vertex of vertices) {
      const root = find(vertex); // 各頂点の最終的な根を見つける
      if (!(root in components)) {
        components[root] = [];
      }
      components[root].push(vertex);
    }

    // 連結成分の配列（値の部分）を返す
    return Object.values(components);
  }
}

function main() {
  console.log("UnionFind TEST -----> start");

  console.log("\nnew");
  const graphData = new GraphData();
  console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);

  console.log("\nadd_edge");
  graphData.clear();
  const inputList1 = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]];
  for (const input of inputList1) {
    console.log(`  入力値: ${JSON.stringify(input)}`);
    const output = graphData.addEdge(...input);
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
    const output = graphData.addEdge(...input);
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
    const output = graphData.addEdge(...input);
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
    const output = graphData.addEdge(...input);
    console.log(`  出力値: ${output}`);
  }
  console.log(`  現在のデータ: ${JSON.stringify(graphData.get())}`);
  console.log("\nget_connected_components");
  const output4 = graphData.getConnectedComponents();
  console.log(`  連結成分: ${JSON.stringify(output4)}`);

  console.log("\nUnionFind TEST <----- end");
}

// Nodeの場合は直接実行、ブラウザの場合はwindowオブジェクトに関数を追加
if (typeof module !== 'undefined' && module.exports) {
  // Node.js環境
  if (require.main === module) {
    main();
  }
  module.exports = { GraphData };
} else {
  // ブラウザ環境
  window.main = main;
  window.GraphData = GraphData;
}