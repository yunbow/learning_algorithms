// JavaScript
// グラフの最小全域木: クラスカル法 (Kruskal)

class DSU {
  constructor(vertices) {
    // 各頂点の親を格納します。最初は各頂点自身が親です。
    this.parent = {};
    vertices.forEach(v => {
      this.parent[v] = v;
    });
    // ランク（木の高さまたはサイズ）を格納し、union操作を最適化します。
    this.rank = {};
    vertices.forEach(v => {
      this.rank[v] = 0;
    });
  }

  // 頂点 i が属する集合の代表元（根）を見つけます。
  // パス圧縮により効率化されます。
  find(i) {
    if (this.parent[i] === i) {
      return i;
    }
    // パス圧縮: 探索中に encountered したノードを根に直接つなぎ直します。
    this.parent[i] = this.find(this.parent[i]);
    return this.parent[i];
  }

  // 頂点 i と 頂点 j を含む二つの集合を結合します。
  // ランクによるunionにより効率化されます。
  union(i, j) {
    const root_i = this.find(i);
    const root_j = this.find(j);

    // 根が異なる場合のみ結合します（同じ集合に属している場合は何もしません）。
    if (root_i !== root_j) {
      // ランクが小さい方の木を、ランクが大きい方の木の根に付けます。
      if (this.rank[root_i] < this.rank[root_j]) {
        this.parent[root_i] = root_j;
      } else if (this.rank[root_i] > this.rank[root_j]) {
        this.parent[root_j] = root_i;
      } else {
        // ランクが同じ場合はどちらかを親とし、その親のランクを増やします。
        this.parent[root_j] = root_i;
        this.rank[root_i] += 1;
      }
      return true; // 集合が結合された
    }
    return false; // 既に同じ集合に属していた
  }
}

// 重みを扱えるように改変された GraphData クラス
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
        edges.add(JSON.stringify([edge[0], edge[1], weight])); // [u, v, weight] の形式で格納
      }
    }
    return Array.from(edges).map(JSON.parse);
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
    // 既に同じ辺が存在する場合は重みを更新するかどうか？
    // 今回は単純に追加する（重複辺はプリム法では問題にならないが、データとしては綺麗でないかも）
    // ここでは、同じ頂点間の辺が存在しない場合のみ追加するように修正します。
    
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
    // グラフを空にします。
    this._data = {};
    return true;
  }

  getMst() {
    // 1. 全ての辺を取得し、重みでソートします。
    // getEdges() は [u, v, weight] のリストを返します。
    const edges = this.getEdges();
    // 重み (配列の3番目の要素) をキーとして辺をソート
    const sortedEdges = edges.sort((a, b) => a[2] - b[2]);

    // 2. Union-Findデータ構造を初期化します。
    // 各頂点が自身の集合に属するようにします。
    const vertices = this.getVertices();
    const dsu = new DSU(vertices);

    // 3. MSTを構築します。
    // 結果として得られるMSTの辺を格納するリスト
    const mstEdges = [];
    // MSTに追加された辺の数 (頂点数-1 になればMSTが完成)
    let edgesCount = 0;

    // ソートされた辺を順番に調べます。
    for (const [u, v, weight] of sortedEdges) {
      // 辺 (u, v) の両端点が属する集合の代表元（根）を見つけます。
      const rootU = dsu.find(u);
      const rootV = dsu.find(v);

      // 両端点が異なる集合に属する場合、その辺をMSTに追加してもサイクルは形成されません。
      if (rootU !== rootV) {
        // 辺をMSTに追加します。
        mstEdges.push([u, v, weight]);
        // 辺を追加したので、両端点の集合を結合します。
        dsu.union(u, v);
        // MSTに追加した辺の数を増やします。
        edgesCount += 1;

        // 頂点数から1を引いた数の辺がMSTに追加されたら終了です。
        // これはグラフが連結である場合に限り、完全なMSTが得られたことを意味します。
        // 連結でない場合は、全ての辺を調べ終えるまで続行し、最小全域森を得ます。
        if (edgesCount === vertices.length - 1) {
          break;
        }
      }
    }

    // MST (または最小全域森) の辺のリストを返します。
    return mstEdges;
  }
}

function main() {
  console.log("Kruskal TEST -----> start");
  const graphData = new GraphData();

  graphData.clear();
  const inputList1 = [['A', 'B', 4], ['B', 'C', 3], ['B', 'D', 2], ['D', 'A', 1], ['A', 'C', 2], ['B', 'D', 2]];
  for (const input of inputList1) {
    graphData.addEdge(...input);
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
    graphData.addEdge(...input);
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
    graphData.addEdge(...input);
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
    graphData.addEdge(...input);
  }
  console.log("\nグラフの頂点:", graphData.getVertices());
  console.log("グラフの辺 (重み付き):", graphData.getEdges());
  const outputMst4 = graphData.getMst();
  for (const edge of outputMst4) {
    console.log(`Edge: ${edge[0]} - ${edge[1]}, Weight: ${edge[2]}`);
  }
  const totalWeight4 = outputMst4.reduce((sum, edge) => sum + edge[2], 0);
  console.log(`最小全域木の合計重み: ${totalWeight4}`);

  console.log("\nKruskal TEST <----- end");
}

main();