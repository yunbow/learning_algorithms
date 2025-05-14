// C#
// グラフの最小全域木: クラスカル法 (Kruskal)

using System;
using System.Collections.Generic;
using System.Linq;

class DSU
{
    private Dictionary<string, string> parent;
    private Dictionary<string, int> rank;

    public DSU(IEnumerable<string> vertices)
    {
        // 各頂点の親を格納します。最初は各頂点自身が親です。
        parent = vertices.ToDictionary(v => v, v => v);
        // ランク（木の高さまたはサイズ）を格納し、union操作を最適化します。
        rank = vertices.ToDictionary(v => v, v => 0);
    }

    // 頂点 i が属する集合の代表元（根）を見つけます。
    // パス圧縮により効率化されます。
    public string Find(string i)
    {
        if (parent[i] == i)
            return i;
        // パス圧縮: 探索中に encountered したノードを根に直接つなぎ直します。
        parent[i] = Find(parent[i]);
        return parent[i];
    }

    // 頂点 i と 頂点 j を含む二つの集合を結合します。
    // ランクによるunionにより効率化されます。
    public bool Union(string i, string j)
    {
        string rootI = Find(i);
        string rootJ = Find(j);

        // 根が異なる場合のみ結合します（同じ集合に属している場合は何もしません）。
        if (rootI != rootJ)
        {
            // ランクが小さい方の木を、ランクが大きい方の木の根に付けます。
            if (rank[rootI] < rank[rootJ])
            {
                parent[rootI] = rootJ;
            }
            else if (rank[rootI] > rank[rootJ])
            {
                parent[rootJ] = rootI;
            }
            else
            {
                // ランクが同じ場合はどちらかを親とし、その親のランクを増やします。
                parent[rootJ] = rootI;
                rank[rootI]++;
            }
            return true; // 集合が結合された
        }
        return false; // 既に同じ集合に属していた
    }
}

// 重みを扱えるように改変された GraphData クラス
class GraphData
{
    // 隣接ノードとその辺の重みを格納します。
    private Dictionary<string, List<Tuple<string, int>>> _data;

    public GraphData()
    {
        _data = new Dictionary<string, List<Tuple<string, int>>>();
    }

    public Dictionary<string, List<Tuple<string, int>>> Get()
    {
        // グラフの内部データを取得します。
        return _data;
    }

    public List<string> GetVertices()
    {
        // グラフの全頂点をリストとして返します。
        return _data.Keys.ToList();
    }

    public List<Tuple<string, string, int>> GetEdges()
    {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにハッシュセットを使用します。
        HashSet<Tuple<string, string, int>> edges = new HashSet<Tuple<string, string, int>>();

        foreach (var vertex in _data.Keys)
        {
            foreach (var neighborInfo in _data[vertex])
            {
                string neighbor = neighborInfo.Item1;
                int weight = neighborInfo.Item2;
                
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                string[] sortedVertices = new string[] { vertex, neighbor };
                Array.Sort(sortedVertices);
                
                edges.Add(new Tuple<string, string, int>(sortedVertices[0], sortedVertices[1], weight));
            }
        }

        return edges.ToList();
    }

    public bool AddVertex(string vertex)
    {
        // 新しい頂点をグラフに追加します。
        if (!_data.ContainsKey(vertex))
        {
            _data[vertex] = new List<Tuple<string, int>>();
            return true;
        }
        // 既に存在する場合は追加しないがTrueを返す（変更なしでも成功とみなす）
        return true;
    }

    public bool AddEdge(string vertex1, string vertex2, int weight)
    {
        // 両頂点間に辺を追加します。重みを指定します。
        // 頂点がグラフに存在しない場合は追加します。
        if (!_data.ContainsKey(vertex1))
        {
            AddVertex(vertex1);
        }
        if (!_data.ContainsKey(vertex2))
        {
            AddVertex(vertex2);
        }

        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edgeExistsV1V2 = false;
        for (int i = 0; i < _data[vertex1].Count; i++)
        {
            if (_data[vertex1][i].Item1 == vertex2)
            {
                _data[vertex1][i] = new Tuple<string, int>(vertex2, weight); // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2)
        {
            _data[vertex1].Add(new Tuple<string, int>(vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edgeExistsV2V1 = false;
        for (int i = 0; i < _data[vertex2].Count; i++)
        {
            if (_data[vertex2][i].Item1 == vertex1)
            {
                _data[vertex2][i] = new Tuple<string, int>(vertex1, weight); // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true;
                break;
            }
        }
        if (!edgeExistsV2V1)
        {
            _data[vertex2].Add(new Tuple<string, int>(vertex1, weight));
        }

        return true;
    }

    public bool Clear()
    {
        // グラフを空にします。
        _data.Clear();
        return true;
    }

    public List<Tuple<string, string, int>> GetMST()
    {
        // 1. 全ての辺を取得し、重みでソートします。
        var edges = GetEdges();
        // 重み (タプルの3番目の要素) をキーとして辺をソート
        var sortedEdges = edges.OrderBy(edge => edge.Item3).ToList();

        // 2. Union-Findデータ構造を初期化します。
        // 各頂点が自身の集合に属するようにします。
        var vertices = GetVertices();
        var dsu = new DSU(vertices);

        // 3. MSTを構築します。
        // 結果として得られるMSTの辺を格納するリスト
        var mstEdges = new List<Tuple<string, string, int>>();
        // MSTに追加された辺の数 (頂点数-1 になればMSTが完成)
        int edgesCount = 0;

        // ソートされた辺を順番に調べます。
        foreach (var edge in sortedEdges)
        {
            string u = edge.Item1;
            string v = edge.Item2;
            int weight = edge.Item3;

            // 辺 (u, v) の両端点が属する集合の代表元（根）を見つけます。
            string rootU = dsu.Find(u);
            string rootV = dsu.Find(v);

            // 両端点が異なる集合に属する場合、その辺をMSTに追加してもサイクルは形成されません。
            if (rootU != rootV)
            {
                // 辺をMSTに追加します。
                mstEdges.Add(edge);
                // 辺を追加したので、両端点の集合を結合します。
                dsu.Union(u, v);
                // MSTに追加した辺の数を増やします。
                edgesCount++;

                // 頂点数から1を引いた数の辺がMSTに追加されたら終了です。
                // これはグラフが連結である場合に限り、完全なMSTが得られたことを意味します。
                // 連結でない場合は、全ての辺を調べ終えるまで続行し、最小全域森を得ます。
                if (edgesCount == vertices.Count - 1)
                {
                    break;
                }
            }
        }

        // MST (または最小全域森) の辺のリストを返します。
        return mstEdges;
    }
}

class Program
{
    static void Main()
    {
        Console.WriteLine("Kruskal TEST -----> start");
        GraphData graphData = new GraphData();

        graphData.Clear();
        var inputList1 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("B", "C", 3),
            new Tuple<string, string, int>("B", "D", 2),
            new Tuple<string, string, int>("D", "A", 1),
            new Tuple<string, string, int>("A", "C", 2),
            new Tuple<string, string, int>("B", "D", 2)
        };
        foreach (var input in inputList1)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")) + "]");
        var outputMst1 = graphData.GetMST();
        foreach (var edge in outputMst1)
        {
            Console.WriteLine($"Edge: {edge.Item1} - {edge.Item2}, Weight: {edge.Item3}");
        }
        int totalWeight1 = outputMst1.Sum(edge => edge.Item3);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight1}");

        graphData.Clear();
        var inputList2 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("C", "D", 4),
            new Tuple<string, string, int>("E", "F", 1),
            new Tuple<string, string, int>("F", "G", 1)
        };
        foreach (var input in inputList2)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")) + "]");
        var outputMst2 = graphData.GetMST();
        foreach (var edge in outputMst2)
        {
            Console.WriteLine($"Edge: {edge.Item1} - {edge.Item2}, Weight: {edge.Item3}");
        }
        int totalWeight2 = outputMst2.Sum(edge => edge.Item3);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight2}");

        graphData.Clear();
        var inputList3 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("B", "C", 3),
            new Tuple<string, string, int>("D", "E", 5)
        };
        foreach (var input in inputList3)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")) + "]");
        var outputMst3 = graphData.GetMST();
        foreach (var edge in outputMst3)
        {
            Console.WriteLine($"Edge: {edge.Item1} - {edge.Item2}, Weight: {edge.Item3}");
        }
        int totalWeight3 = outputMst3.Sum(edge => edge.Item3);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight3}");

        graphData.Clear();
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")) + "]");
        var outputMst4 = graphData.GetMST();
        foreach (var edge in outputMst4)
        {
            Console.WriteLine($"Edge: {edge.Item1} - {edge.Item2}, Weight: {edge.Item3}");
        }
        int totalWeight4 = outputMst4.Sum(edge => edge.Item3);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight4}");

        Console.WriteLine("\nKruskal TEST <----- end");
    }
}