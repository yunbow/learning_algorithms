// C#
// グラフの最短経路: ダイクストラ法 (dijkstra)

using System;
using System.Collections.Generic;
using System.Linq;

// グラフの最短経路: ダイクストラ法 (dijkstra)
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
        var edges = new HashSet<Tuple<string, string, int>>();
        
        foreach (var vertex in _data.Keys)
        {
            foreach (var neighborInfo in _data[vertex])
            {
                var neighbor = neighborInfo.Item1;
                var weight = neighborInfo.Item2;
                
                // 辺を正規化してセットに追加 (小さい方の頂点を最初にするなど)
                string[] sortedVertices = new[] { vertex, neighbor };
                Array.Sort(sortedVertices);
                
                edges.Add(new Tuple<string, string, int>(sortedVertices[0], sortedVertices[1], weight));
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
            Console.WriteLine($"ERROR: 開始頂点 '{startVertex}' または 終了頂点 '{endVertex}' がグラフに存在しません。");
            return new Tuple<List<string>, int>(null, int.MaxValue);
        }

        // 距離を初期化: 全ての頂点への距離を無限大に設定し、開始頂点のみ0とする
        var distances = new Dictionary<string, int>();
        foreach (var vertex in _data.Keys)
        {
            distances[vertex] = int.MaxValue;
        }
        distances[startVertex] = 0;

        // 経路を記録するための辞書: 各頂点に至る直前の頂点を保持
        var predecessors = new Dictionary<string, string>();
        foreach (var vertex in _data.Keys)
        {
            predecessors[vertex] = null;
        }

        // 優先度付きキュー: C#のPriorityQueueを使用
        // .NET 6以上であればPriorityQueue<TElement, TPriority>クラスを使用できる
        // ここでは代替としてSortedSetを使う
        var priorityQueue = new SortedSet<Tuple<int, string>>(
            Comparer<Tuple<int, string>>.Create((a, b) => 
            {
                int distanceComparison = a.Item1.CompareTo(b.Item1);
                // 距離が同じ場合は頂点で比較（重複を避けるため）
                return distanceComparison != 0 ? distanceComparison : a.Item2.CompareTo(b.Item2);
            })
        );
        
        priorityQueue.Add(new Tuple<int, string>(0, startVertex)); // (開始頂点への距離, 開始頂点)

        while (priorityQueue.Count > 0)
        {
            // 優先度付きキューから最も距離の小さい頂点を取り出す
            var current = priorityQueue.Min;
            priorityQueue.Remove(current);
            int currentDistance = current.Item1;
            string currentVertex = current.Item2;

            // 取り出した頂点への距離が、すでに記録されている距離より大きい場合は、
            // より短い経路が既に見つかっているためスキップ
            if (currentDistance > distances[currentVertex])
            {
                continue;
            }

            // 終了頂点に到達したら探索終了
            if (currentVertex == endVertex)
            {
                break; // 最短経路が見つかった
            }

            // 現在の頂点から到達可能な隣接頂点を探索
            var neighbors = GetNeighbors(currentVertex);
            if (neighbors != null)
            {
                foreach (var neighborInfo in neighbors)
                {
                    string neighbor = neighborInfo.Item1;
                    int weight = neighborInfo.Item2;
                    int distanceThroughCurrent = distances[currentVertex] + weight;

                    // より短い経路が見つかった場合
                    if (distanceThroughCurrent < distances[neighbor])
                    {
                        distances[neighbor] = distanceThroughCurrent;
                        predecessors[neighbor] = currentVertex;
                        // 優先度付きキューに隣接頂点を追加または更新
                        // ダイクストラ法では heuristic は使用しない (または h=0)
                        priorityQueue.Add(new Tuple<int, string>(distanceThroughCurrent, neighbor));
                    }
                }
            }
        }

        // 終了頂点への最短距離が無限大のままなら、到達不可能
        if (distances[endVertex] == int.MaxValue)
        {
            Console.WriteLine($"INFO: 開始頂点 '{startVertex}' から 終了頂点 '{endVertex}' への経路は存在しません。");
            return new Tuple<List<string>, int>(null, int.MaxValue);
        }

        // 最短経路を再構築
        var path = new List<string>();
        string current = endVertex;
        while (current != null)
        {
            path.Add(current);
            current = predecessors[current];
        }
        path.Reverse(); // 経路は逆順に構築されたので反転

        // 開始ノードから開始されていることを確認
        if (path[0] != startVertex)
        {
            // これが発生するのは、開始頂点が存在しないか、到達不能な場合だが、
            // 前段のチェックで大部分はカバーされているはず。
            // ここに来る場合は、特殊なケース（例えば孤立した開始点と終了点）が考えられる。
            return new Tuple<List<string>, int>(null, int.MaxValue);
        }

        return new Tuple<List<string>, int>(path, distances[endVertex]);
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
        Console.WriteLine("Dijkstra -----> start");

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
        
        var input = new Tuple<string, string>("A", "B");
        var shortestPath = graphData.GetShortestPath(input.Item1, input.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input.Item1}-{input.Item2} の最短経路は [{string.Join(", ", shortestPath.Item1 ?? new List<string>())}] (重み: {(shortestPath.Item2 == int.MaxValue ? "inf" : shortestPath.Item2.ToString())})");

        graphData.Clear();
        var inputList2 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("C", "D", 4),
            new Tuple<string, string, int>("E", "F", 1),
            new Tuple<string, string, int>("F", "G", 1)
        };
        
        foreach (var inp in inputList2)
        {
            graphData.AddEdge(inp.Item1, inp.Item2, inp.Item3);
        }
        
        Console.WriteLine("\nグラフの頂点: [{0}]", string.Join(", ", graphData.GetVertices()));
        Console.WriteLine("グラフの辺 (重み付き): [{0}]", string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
        
        input = new Tuple<string, string>("A", "B");
        shortestPath = graphData.GetShortestPath(input.Item1, input.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input.Item1}-{input.Item2} の最短経路は [{string.Join(", ", shortestPath.Item1 ?? new List<string>())}] (重み: {(shortestPath.Item2 == int.MaxValue ? "inf" : shortestPath.Item2.ToString())})");

        graphData.Clear();
        var inputList3 = new List<Tuple<string, string, int>>
        {
            new Tuple<string, string, int>("A", "B", 4),
            new Tuple<string, string, int>("B", "C", 3),
            new Tuple<string, string, int>("D", "E", 5)
        };
        
        foreach (var inp in inputList3)
        {
            graphData.AddEdge(inp.Item1, inp.Item2, inp.Item3);
        }
        
        Console.WriteLine("\nグラフの頂点: [{0}]", string.Join(", ", graphData.GetVertices()));
        Console.WriteLine("グラフの辺 (重み付き): [{0}]", string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
        
        input = new Tuple<string, string>("A", "D");
        shortestPath = graphData.GetShortestPath(input.Item1, input.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input.Item1}-{input.Item2} の最短経路は [{string.Join(", ", shortestPath.Item1 ?? new List<string>())}] (重み: {(shortestPath.Item2 == int.MaxValue ? "inf" : shortestPath.Item2.ToString())})");

        graphData.Clear();
        // 空のリスト
        
        Console.WriteLine("\nグラフの頂点: [{0}]", string.Join(", ", graphData.GetVertices()));
        Console.WriteLine("グラフの辺 (重み付き): [{0}]", string.Join(", ", graphData.GetEdges().Select(e => $"({e.Item1}, {e.Item2}, {e.Item3})")));
        
        input = new Tuple<string, string>("A", "B");
        shortestPath = graphData.GetShortestPath(input.Item1, input.Item2, DummyHeuristic);
        Console.WriteLine($"経路{input.Item1}-{input.Item2} の最短経路は [{string.Join(", ", shortestPath.Item1 ?? new List<string>())}] (重み: {(shortestPath.Item2 == int.MaxValue ? "inf" : shortestPath.Item2.ToString())})");

        Console.WriteLine("\nDijkstra <----- end");
    }
}