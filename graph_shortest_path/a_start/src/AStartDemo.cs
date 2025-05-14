// C#
// グラフの最短経路: A-star

using System;
using System.Collections.Generic;
using System.Linq;

class GraphData
{
    // 隣接ノードとその辺の重みを格納します。
    // キーは頂点、値はその頂点に隣接する頂点と重みのリストです。
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
        // 重複を避けるためにセットを使用します。
        var edges = new HashSet<Tuple<string, string, int>>();
        foreach (var vertex in _data.Keys)
        {
            foreach (var neighbor in _data[vertex])
            {
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                var sortedVertices = new[] { vertex, neighbor.Item1 }.OrderBy(v => v).ToArray();
                edges.Add(new Tuple<string, string, int>(sortedVertices[0], sortedVertices[1], neighbor.Item2)); // (u, v, weight) の形式で格納
            }
        }
        return edges.ToList();
    }

    public List<Tuple<string, int>> GetNeighbors(string vertex)
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

        // 両方向に辺を追加する（無向グラフ）
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

    public Tuple<List<string>, int> GetShortestPath(string startVertex, string endVertex, Func<string, string, int> heuristic)
    {
        if (!_data.ContainsKey(startVertex) || !_data.ContainsKey(endVertex))
        {
            Console.WriteLine("ERROR: 開始頂点または終了頂点がグラフに存在しません。");
            return new Tuple<List<string>, int>(null, int.MaxValue);
        }

        if (startVertex == endVertex)
        {
            return new Tuple<List<string>, int>(new List<string> { startVertex }, 0);
        }

        // g_costs: 開始ノードから各ノードまでの既知の最短コスト
        var gCosts = new Dictionary<string, int>();
        foreach (var vertex in _data.Keys)
        {
            gCosts[vertex] = int.MaxValue;
        }
        gCosts[startVertex] = 0;

        // f_costs: g_costs + ヒューリスティックコスト (推定合計コスト)
        var fCosts = new Dictionary<string, int>();
        foreach (var vertex in _data.Keys)
        {
            fCosts[vertex] = int.MaxValue;
        }
        fCosts[startVertex] = heuristic(startVertex, endVertex);

        // came_from: 最短経路で各ノードの直前のノードを記録
        var cameFrom = new Dictionary<string, string>();

        // open_set: 探索すべきノードの優先度キュー
        var openSet = new SortedSet<Tuple<int, string>>(Comparer<Tuple<int, string>>.Create((a, b) =>
        {
            int compare = a.Item1.CompareTo(b.Item1);
            return compare != 0 ? compare : a.Item2.CompareTo(b.Item2); // Tuple<int, string>の比較
        }));
        openSet.Add(new Tuple<int, string>(fCosts[startVertex], startVertex));

        while (openSet.Count > 0)
        {
            // open_set から最も f_cost が低いノードを取り出す
            var current = openSet.Min;
            openSet.Remove(current);
            int currentFCost = current.Item1;
            string currentVertex = current.Item2;

            // 取り出したノードの f_cost が、記録されている f_costs[current_vertex] より大きい場合、
            // それは古い情報なので無視して次のノードに進む
            if (currentFCost > fCosts[currentVertex])
            {
                continue;
            }

            // 目標ノードに到達した場合、経路を再構築して返す
            if (currentVertex == endVertex)
            {
                return new Tuple<List<string>, int>(ReconstructPath(cameFrom, endVertex), gCosts[endVertex]);
            }

            // 現在のノードの隣接ノードを調べる
            var neighbors = GetNeighbors(currentVertex);
            if (neighbors == null) // 孤立したノードの場合など
            {
                continue;
            }

            foreach (var neighborPair in neighbors)
            {
                string neighbor = neighborPair.Item1;
                int weight = neighborPair.Item2;

                // 現在のノードを経由した場合の隣接ノードへの新しい g_cost
                int tentativeGCost = gCosts[currentVertex] + weight;

                // 新しい g_cost が、現在記録されている隣接ノードへの g_cost よりも小さい場合
                if (tentativeGCost < gCosts[neighbor])
                {
                    // 経路情報を更新
                    cameFrom[neighbor] = currentVertex;
                    gCosts[neighbor] = tentativeGCost;
                    fCosts[neighbor] = gCosts[neighbor] + heuristic(neighbor, endVertex);

                    // 隣接ノードを open_set に追加（または優先度を更新）
                    var nodeEntry = new Tuple<int, string>(fCosts[neighbor], neighbor);
                    openSet.Add(nodeEntry);
                }
            }
        }

        // open_set が空になっても目標ノードに到達しなかった場合、経路は存在しない
        return new Tuple<List<string>, int>(null, int.MaxValue);
    }

    private List<string> ReconstructPath(Dictionary<string, string> cameFrom, string currentVertex)
    {
        var path = new List<string>();
        while (cameFrom.ContainsKey(currentVertex))
        {
            path.Add(currentVertex);
            currentVertex = cameFrom[currentVertex];
        }
        path.Add(currentVertex); // 開始ノードを追加
        path.Reverse(); // 経路を逆順にする (開始 -> 目標)
        return path;
    }
}

class Program
{
    // ヒューリスティック関数 (この例では常に0、ダイクストラ法と同じ)
    // 実際のA*では、問題に応じた適切な推定関数を使用する必要があります。
    static int DummyHeuristic(string u, string v)
    {
        // u と v の間に何らかの推定距離を計算する関数
        // ここではダミーとして常に0を返す
        return 0;
    }

    static void Main(string[] args)
    {
        Console.WriteLine("A-start TEST -----> start");

        var graphData = new GraphData();

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
        Console.WriteLine("\nグラフの頂点: [{0}]", string.Join(", ", graphData.GetVertices()));
        Console.WriteLine("グラフの辺 (重み付き): [{0}]", string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
        var input1 = new Tuple<string, string>("A", "B");
        var shortestPath1 = graphData.GetShortestPath(input1.Item1, input1.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input1.Item1}-{input1.Item2} の最短経路は [{string.Join(", ", shortestPath1.Item1)}] (重み: {shortestPath1.Item2})");

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
        Console.WriteLine("\nグラフの頂点: [{0}]", string.Join(", ", graphData.GetVertices()));
        Console.WriteLine("グラフの辺 (重み付き): [{0}]", string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
        var input2 = new Tuple<string, string>("A", "B");
        var shortestPath2 = graphData.GetShortestPath(input2.Item1, input2.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input2.Item1}-{input2.Item2} の最短経路は [{string.Join(", ", shortestPath2.Item1)}] (重み: {shortestPath2.Item2})");

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
        Console.WriteLine("\nグラフの頂点: [{0}]", string.Join(", ", graphData.GetVertices()));
        Console.WriteLine("グラフの辺 (重み付き): [{0}]", string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
        var input3 = new Tuple<string, string>("A", "D");
        var shortestPath3 = graphData.GetShortestPath(input3.Item1, input3.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input3.Item1}-{input3.Item2} の最短経路は [{(shortestPath3.Item1 != null ? string.Join(", ", shortestPath3.Item1) : "None")}] (重み: {shortestPath3.Item2})");

        graphData.Clear();
        var inputList4 = new List<Tuple<string, string, int>>();
        foreach (var input in inputList4)
        {
            graphData.AddEdge(input.Item1, input.Item2, input.Item3);
        }
        Console.WriteLine("\nグラフの頂点: [{0}]", string.Join(", ", graphData.GetVertices()));
        Console.WriteLine("グラフの辺 (重み付き): [{0}]", string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
        var input4 = new Tuple<string, string>("A", "B");
        var shortestPath4 = graphData.GetShortestPath(input4.Item1, input4.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input4.Item1}-{input4.Item2} の最短経路は [{(shortestPath4.Item1 != null ? string.Join(", ", shortestPath4.Item1) : "None")}] (重み: {shortestPath4.Item2})");

        Console.WriteLine("\nA-start TEST <----- end");
    }
}