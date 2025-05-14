// C#
// グラフの最小全域木: プリム法 (Prim)

using System;
using System.Collections.Generic;
using System.Linq;

class GraphData
{
    // 隣接リストとしてグラフデータを格納します。
    // キーは頂点、値はその頂点に隣接する頂点とその辺の重みのタプルのリストです。
    private Dictionary<string, List<(string Neighbor, int Weight)>> _data;

    public GraphData()
    {
        _data = new Dictionary<string, List<(string, int)>>();
    }

    public Dictionary<string, List<(string, int)>> Get()
    {
        // グラフの内部データを取得します。
        return _data;
    }

    public List<string> GetVertices()
    {
        // グラフの全頂点をリストとして返します。
        return _data.Keys.ToList();
    }

    public List<(string Vertex1, string Vertex2, int Weight)> GetEdges()
    {
        // グラフの全辺をリストとして返します。
        // 無向グラフの場合、(u, v, weight) の形式で返します。
        // 重複を避けるためにハッシュセットを使用します。
        var edges = new HashSet<(string, string, int)>();
        foreach (var vertex in _data.Keys)
        {
            foreach (var (neighbor, weight) in _data[vertex])
            {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                var normalizedVertices = new[] { vertex, neighbor }.OrderBy(v => v).ToArray();
                edges.Add((normalizedVertices[0], normalizedVertices[1], weight));
            }
        }
        return edges.ToList();
    }

    public List<(string Neighbor, int Weight)> GetNeighbors(string vertex)
    {
        // 指定された頂点の隣接ノードと辺の重みのリストを返します。
        // 形式: [(隣接頂点, 重み), ...]
        if (_data.ContainsKey(vertex))
        {
            return _data[vertex];
        }
        else
        {
            return null; // 頂点が存在しない場合はNullを返す
        }
    }

    public int? GetEdgeWeight(string vertex1, string vertex2)
    {
        // 指定された2つの頂点間の辺の重みを返します。
        // 辺が存在しない場合はnullを返します。
        if (_data.ContainsKey(vertex1) && _data.ContainsKey(vertex2))
        {
            foreach (var (neighbor, weight) in _data[vertex1])
            {
                if (neighbor == vertex2)
                {
                    return weight;
                }
            }
        }
        return null; // 辺が存在しない場合
    }

    public bool AddVertex(string vertex)
    {
        // 新しい頂点をグラフに追加します。
        if (!_data.ContainsKey(vertex))
        {
            _data[vertex] = new List<(string, int)>();
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

        // 両方向に辺を追加する（無向グラフ）
        // 既に同じ辺が存在する場合は重みを更新する

        // vertex1 -> vertex2 の辺を追加（重み付き）
        bool edgeExistsV1V2 = false;
        for (int i = 0; i < _data[vertex1].Count; i++)
        {
            if (_data[vertex1][i].Neighbor == vertex2)
            {
                _data[vertex1][i] = (vertex2, weight); // 既に存在する場合は重みを更新
                edgeExistsV1V2 = true;
                break;
            }
        }
        if (!edgeExistsV1V2)
        {
            _data[vertex1].Add((vertex2, weight));
        }

        // vertex2 -> vertex1 の辺を追加（重み付き）
        bool edgeExistsV2V1 = false;
        for (int i = 0; i < _data[vertex2].Count; i++)
        {
            if (_data[vertex2][i].Neighbor == vertex1)
            {
                _data[vertex2][i] = (vertex1, weight); // 既に存在する場合は重みを更新
                edgeExistsV2V1 = true;
                break;
            }
        }
        if (!edgeExistsV2V1)
        {
            _data[vertex2].Add((vertex1, weight));
        }

        return true;
    }

    public bool IsEmpty()
    {
        // グラフが空かどうかを返します。
        return _data.Count == 0;
    }

    public bool Clear()
    {
        // グラフを空にします。
        _data.Clear();
        return true;
    }

    public List<(string Vertex1, string Vertex2, int Weight)> GetMST(string startVertex = null)
    {
        List<string> vertices = GetVertices();
        if (vertices.Count == 0)
        {
            return new List<(string, string, int)>(); // グラフが空
        }

        if (startVertex == null)
        {
            startVertex = vertices[0];
        }
        else if (!_data.ContainsKey(startVertex))
        {
            Console.WriteLine($"ERROR: 開始頂点 {startVertex} はグラフに存在しません。");
            return null;
        }

        // MSTに含まれる頂点のセット
        var inMST = new HashSet<string>();
        // 優先度付きキュー (重み, 現在の頂点, 遷移元の頂点)
        // C#のPriorityQueueがない場合は、SortedSetやSortedDictionaryを使用する
        var minHeap = new SortedSet<(int Weight, string Vertex, string FromVertex, int Order)>(
            Comparer<(int Weight, string Vertex, string FromVertex, int Order)>.Create((a, b) => {
                if (a.Weight != b.Weight) return a.Weight.CompareTo(b.Weight);
                return a.Order.CompareTo(b.Order); // 同じ重みの場合は追加順で比較
            })
        );
        // MSTを構成する辺のリスト
        var mstEdges = new List<(string, string, int)>();
        // 各頂点への最小コスト（MSTに追加する際の辺の重み）
        var minCost = new Dictionary<string, int>();
        foreach (var v in vertices)
        {
            minCost[v] = int.MaxValue;
        }
        // 親頂点の記録
        var parent = new Dictionary<string, string>();
        foreach (var v in vertices)
        {
            parent[v] = null;
        }

        // 開始頂点の処理
        minCost[startVertex] = 0;
        int order = 0;
        minHeap.Add((0, startVertex, null, order++));

        while (minHeap.Count > 0)
        {
            // 最小コストの辺を持つ頂点を取り出す
            var (cost, currentVertex, fromVertex, _) = minHeap.Min;
            minHeap.Remove(minHeap.Min);

            // 既にMSTに含まれている頂点であればスキップ
            if (inMST.Contains(currentVertex))
            {
                continue;
            }

            // 現在の頂点をMSTに追加
            inMST.Add(currentVertex);

            // MSTに追加された辺を記録 (開始頂点以外)
            if (fromVertex != null)
            {
                // from_vertex から current_vertex への辺の重みを取得
                int? weight = GetEdgeWeight(fromVertex, currentVertex);
                if (weight.HasValue)
                {
                    var normalizedVertices = new[] { fromVertex, currentVertex }.OrderBy(v => v).ToArray();
                    mstEdges.Add((normalizedVertices[0], normalizedVertices[1], weight.Value));
                }
            }

            // 現在の頂点に隣接する頂点を調べ、MSTへの追加コストを更新
            var neighborsWithWeight = GetNeighbors(currentVertex);
            if (neighborsWithWeight != null) // 隣接する頂点がある場合
            {
                foreach (var (neighbor, weight) in neighborsWithWeight)
                {
                    // 隣接頂点がまだMSTに含まれておらず、現在のコストよりも小さい場合
                    if (!inMST.Contains(neighbor) && weight < minCost[neighbor])
                    {
                        minCost[neighbor] = weight;
                        parent[neighbor] = currentVertex;
                        minHeap.Add((weight, neighbor, currentVertex, order++));
                    }
                }
            }
        }

        return mstEdges;
    }
}

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Prims TEST -----> start");
        var graphData = new GraphData();

        graphData.Clear();
        var inputList1 = new[]
        {
            ("A", "B", 4),
            ("B", "C", 3),
            ("B", "D", 2),
            ("D", "A", 1),
            ("A", "C", 2),
            ("B", "D", 2)
        };
        foreach (var input in inputList1)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Vertex1}, {e.Vertex2}, {e.Weight})")) + "]");
        var outputMst1 = graphData.GetMST();
        foreach (var edge in outputMst1)
        {
            Console.WriteLine($"Edge: {edge.Vertex1} - {edge.Vertex2}, Weight: {edge.Weight}");
        }
        int totalWeight1 = outputMst1.Sum(edge => edge.Weight);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight1}");

        graphData.Clear();
        var inputList2 = new[]
        {
            ("A", "B", 4),
            ("C", "D", 4),
            ("E", "F", 1),
            ("F", "G", 1)
        };
        foreach (var input in inputList2)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Vertex1}, {e.Vertex2}, {e.Weight})")) + "]");
        var outputMst2 = graphData.GetMST();
        foreach (var edge in outputMst2)
        {
            Console.WriteLine($"Edge: {edge.Vertex1} - {edge.Vertex2}, Weight: {edge.Weight}");
        }
        int totalWeight2 = outputMst2.Sum(edge => edge.Weight);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight2}");

        graphData.Clear();
        var inputList3 = new[]
        {
            ("A", "B", 4),
            ("B", "C", 3),
            ("D", "E", 5)
        };
        foreach (var input in inputList3)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Vertex1}, {e.Vertex2}, {e.Weight})")) + "]");
        var outputMst3 = graphData.GetMST();
        foreach (var edge in outputMst3)
        {
            Console.WriteLine($"Edge: {edge.Vertex1} - {edge.Vertex2}, Weight: {edge.Weight}");
        }
        int totalWeight3 = outputMst3.Sum(edge => edge.Weight);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight3}");

        graphData.Clear();
        var inputList4 = Array.Empty<(string, string, int)>();
        foreach (var input in inputList4)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [" + string.Join(", ", graphData.GetVertices()) + "]");
        Console.WriteLine("グラフの辺 (重み付き): [" + string.Join(", ", graphData.GetEdges().Select(e => $"({e.Vertex1}, {e.Vertex2}, {e.Weight})")) + "]");
        var outputMst4 = graphData.GetMST();
        foreach (var edge in outputMst4)
        {
            Console.WriteLine($"Edge: {edge.Vertex1} - {edge.Vertex2}, Weight: {edge.Weight}");
        }
        int totalWeight4 = outputMst4.Sum(edge => edge.Weight);
        Console.WriteLine($"最小全域木の合計重み: {totalWeight4}");

        Console.WriteLine("\nPrims TEST <----- end");
    }
}