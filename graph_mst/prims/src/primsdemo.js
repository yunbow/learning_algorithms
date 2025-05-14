// JavaScript
// グラフの最小全域木: プリム法 (Prim)

class GraphData {
  constructor() {
    // 隣接リストとしてグラフデータを格納します。
    // キーは頂点、値はその頂点に隣接する頂点とその辺の重みのタプルのリストです。
    // 例: { 'A': [['B', 1], ['C', 4]], 'B': [['A', 1], ['C', 2]], ... }
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
        edges.add(JSON.stringify([edge[0], edge[1], weight])); // 文字列化してセットに追加
      }
    }
    // JSON文字列から元の配列に戻す
    return Array.from(edges).map(e => JSON.parse(e));
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
    // グラフが空かどうかを返します。
    return Object.keys(this._data).length === 0;
  }

  clear() {
    // グラフを空にします。
    this._data = {};
    return true;
  }

  // JavaScriptではheapqが標準ライブラリにないため、優先度キューを実装
  // 最小ヒープの優先度キューを簡易実装
  _createMinHeap() {
    return {
      values: [],
      insert(value) {
        this.values.push(value);
        this.values.sort((a, b) => a[0] - b[0]); // コスト（重み）に基づいてソート
      },
      extractMin() {
        if (this.values.length === 0) return null;
        return this.values.shift();
      },
      isEmpty() {
        return this.values.length === 0;
      }
    };
  }

  getMst(startVertex = null) {
    const vertices = this.getVertices();
    if (vertices.length === 0) {
      return []; // グラフが空
    }

    if (startVertex === null) {
      startVertex = vertices[0];
    } else if (!(startVertex in this._data)) {
      console.error(`ERROR: 開始頂点 ${startVertex} はグラフに存在しません。`);
      return null;
    }

    // MSTに含まれる頂点のセット
    const inMst = new Set();
    // 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
    const minHeap = this._createMinHeap();
    // MSTを構成する辺のリスト
    const mstEdges = [];
    // 各頂点への最小コスト（MSTに追加する際の辺の重み）と、その遷移元の頂点を記録
    const minCost = {};
    const parent = {};

    for (const v of vertices) {
      minCost[v] = Infinity;
      parent[v] = null;
    }

    // 開始頂点の処理
    minCost[startVertex] = 0;
    minHeap.insert([0, startVertex, null]); // [コスト, 現在の頂点, 遷移元の頂点]

    while (!minHeap.isEmpty()) {
      // 最小コストの辺を持つ頂点を取り出す
      const [cost, currentVertex, fromVertex] = minHeap.extractMin();

      // 既にMSTに含まれている頂点であればスキップ
      if (inMst.has(currentVertex)) {
        continue;
      }

      // 現在の頂点をMSTに追加
      inMst.add(currentVertex);

      // MSTに追加された辺を記録 (開始頂点以外)
      if (fromVertex !== null) {
        // fromVertex から currentVertex への辺の重みを取得
        const weight = this.getEdgeWeight(fromVertex, currentVertex);
        if (weight !== null) {
          const sortedEdge = [fromVertex, currentVertex].sort();
          mstEdges.push([sortedEdge[0], sortedEdge[1], weight]); // 辺を正規化して追加
        }
      }

      // 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
      const neighborsWithWeight = this.getNeighbors(currentVertex);
      if (neighborsWithWeight) { // 隣接する頂点がある場合
        for (const [neighbor, weight] of neighborsWithWeight) {
          // 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
          if (!inMst.has(neighbor) && weight < minCost[neighbor]) {
            minCost[neighbor] = weight;
            parent[neighbor] = currentVertex;
            minHeap.insert([weight, neighbor, currentVertex]);
          }
        }
      }
    }

    return mstEdges;
  }
}

function main() {
  console.log("Prims TEST -----> start");
  const graphData = new GraphData();

  graphData.clear();
  const inputList1 = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]];
  for (const input of inputList1) {
    graphData.addEdge(input[0], input[1], input[2]);
  }
  console.log("\nグラフの頂点:", graphData.getVertices());
  console.log("グラフの辺 (重み付き):", graphData.getEdges());
  const outputMst1 = graphData.getMst();
  for (const edge of outputMst1) {
    console.log(`Edge: ${edge[0]} - ${edge[1]}, Weight: ${edge[2]}`);
  }
  const totalWeight1 = outputMst1.reduce((sum, edge) => sum + edge[2], 0);
  console.log(`最小全域木の合計重み: ${totalWeight1}`);

  graphData.clear();
  const inputList2 = [['A', 'B', 4], ['C', 'D', 4], ['E', 'F', 1], ['F', 'G', 1]];
  for (const input of inputList2) {
    graphData.addEdge(input[0], input[1], input[2]);
  }
  console.log("\nグラフの頂点:", graphData.getVertices());
  console.log("グラフの辺 (重み付き):", graphData.getEdges());
  const outputMst2 = graphData.getMst();
  for (const edge of outputMst2) {
    console.log(`Edge: ${edge[0]} - ${edge[1]}, Weight: ${edge[2]}`);
  }
  const totalWeight2 = outputMst2.reduce((sum, edge) => sum + edge[2], 0);
  console.log(`最小全域木の合計重み: ${totalWeight2}`);

  graphData.clear();
  const inputList3 = [['A', 'B', 4], ['B', 'C', 3], ['D', 'E', 5]];
  for (const input of inputList3) {
    graphData.addEdge(input[0], input[1], input[2]);
  }
  console.log("\nグラフの頂点:", graphData.getVertices());
  console.log("グラフの辺 (重み付き):", graphData.getEdges());
  const outputMst3 = graphData.getMst();
  for (const edge of outputMst3) {
    console.log(`Edge: ${edge[0]} - ${edge[1]}, Weight: ${edge[2]}`);
  }
  const totalWeight3 = outputMst3.reduce((sum, edge) => sum + edge[2], 0);
  console.log(`最小全域木の合計重み: ${totalWeight3}`);

  graphData.clear();
  const inputList4 = [];
  for (const input of inputList4) {
    graphData.addEdge(input[0], input[1], input[2]);
  }
  console.log("\nグラフの頂点:", graphData.getVertices());
  console.log("グラフの辺 (重み付き):", graphData.getEdges());
  const outputMst4 = graphData.getMst();
  for (const edge of outputMst4) {
    console.log(`Edge: ${edge[0]} - ${edge[1]}, Weight: ${edge[2]}`);
  }
  const totalWeight4 = outputMst4.reduce((sum, edge) => sum + edge[2], 0);
  console.log(`最小全域木の合計重み: ${totalWeight4}`);

  console.log("\nPrims TEST <----- end");
}

// メイン関数の実行
main();