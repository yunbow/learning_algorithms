// C#
// グラフの連結成分: BFS

using System;
using System.Collections.Generic;
using System.Linq;

public class GraphData
{
    private Dictionary<string, List<(string Neighbor, int Weight)>> _data = new Dictionary<string, List<(string, int)>>();

    public Dictionary<string, List<(string, int)>> Get() => _data;

    public List<string> GetVertices() => _data.Keys.ToList();

    public List<(string, string, int)> GetEdges()
    {
        var edges = new HashSet<(string, string, int)>();
        foreach (var vertex in _data)
        {
            foreach (var (neighbor, weight) in vertex.Value)
            {
                var edge = new[] { vertex.Key, neighbor }.OrderBy(v => v).ToArray();
                edges.Add((edge[0], edge[1], weight));
            }
        }
        return edges.ToList();
    }

    public List<(string, int)> GetNeighbors(string vertex)
    {
        return _data.ContainsKey(vertex) ? _data[vertex] : null;
    }

    public int? GetEdgeWeight(string vertex1, string vertex2)
    {
        if (_data.ContainsKey(vertex1))
        {
            foreach (var (neighbor, weight) in _data[vertex1])
            {
                if (neighbor == vertex2)
                    return weight;
            }
        }
        return null;
    }

    public List<(string, int)> GetVertice(string vertex)
    {
        if (_data.ContainsKey(vertex))
            return _data[vertex];
        
        Console.WriteLine($"ERROR: {vertex}は範囲外です");
        return null;
    }

    public bool GetEdge(string vertex1, string vertex2)
    {
        return _data.ContainsKey(vertex1) && 
               _data.ContainsKey(vertex2) && 
               _data[vertex1].Any(n => n.Neighbor == vertex2);
    }

    public bool AddVertex(string vertex)
    {
        if (!_data.ContainsKey(vertex))
            _data[vertex] = new List<(string, int)>();
        return true;
    }

    public bool AddEdge(string vertex1, string vertex2, int weight)
    {
        if (!_data.ContainsKey(vertex1))
            AddVertex(vertex1);
        if (!_data.ContainsKey(vertex2))
            AddVertex(vertex2);

        bool edge_exists_v1v2 = false;
        for (int i = 0; i < _data[vertex1].Count; i++)
        {
            if (_data[vertex1][i].Neighbor == vertex2)
            {
                _data[vertex1][i] = (vertex2, weight);
                edge_exists_v1v2 = true;
                break;
            }
        }
        if (!edge_exists_v1v2)
            _data[vertex1].Add((vertex2, weight));

        bool edge_exists_v2v1 = false;
        for (int i = 0; i < _data[vertex2].Count; i++)
        {
            if (_data[vertex2][i].Neighbor == vertex1)
            {
                _data[vertex2][i] = (vertex1, weight);
                edge_exists_v2v1 = true;
                break;
            }
        }
        if (!edge_exists_v2v1)
            _data[vertex2].Add((vertex1, weight));

        return true;
    }

    public bool RemoveVertex(string vertex)
    {
        if (_data.ContainsKey(vertex))
        {
            foreach (var v in _data.Keys.ToList())
            {
                _data[v] = _data[v].Where(n => n.Neighbor != vertex).ToList();
            }
            _data.Remove(vertex);
            return true;
        }
        
        Console.WriteLine($"ERROR: {vertex} は範囲外です");
        return false;
    }

    public bool RemoveEdge(string vertex1, string vertex2)
    {
        if (_data.ContainsKey(vertex1) && _data.ContainsKey(vertex2))
        {
            bool removed = false;
            var originalLenV1 = _data[vertex1].Count;
            _data[vertex1] = _data[vertex1].Where(n => n.Neighbor != vertex2).ToList();
            if (_data[vertex1].Count < originalLenV1)
                removed = true;

            var originalLenV2 = _data[vertex2].Count;
            _data[vertex2] = _data[vertex2].Where(n => n.Neighbor != vertex1).ToList();
            if (_data[vertex2].Count < originalLenV2)
                removed = true;

            return removed;
        }

        Console.WriteLine($"ERROR: {vertex1} または {vertex2} は範囲外です");
        return false;
    }

    public bool IsEmpty() => _data.Count == 0;

    public int Size() => _data.Count;

    public bool Clear()
    {
        _data.Clear();
        return true;
    }

    public List<List<string>> GetConnectedComponents()
    {
        var visited = new HashSet<string>();
        var allComponents = new List<List<string>>();

        var vertices = GetVertices();

        foreach (var vertex in vertices)
        {
            if (!visited.Contains(vertex))
            {
                var currentComponent = new List<string>();
                var queue = new Queue<string>();
                queue.Enqueue(vertex);
                visited.Add(vertex);
                currentComponent.Add(vertex);

                while (queue.Count > 0)
                {
                    var u = queue.Dequeue();
                    var neighborsWithWeight = GetNeighbors(u);

                    if (neighborsWithWeight != null)
                    {
                        foreach (var (neighbor, weight) in neighborsWithWeight)
                        {
                            if (!visited.Contains(neighbor))
                            {
                                visited.Add(neighbor);
                                queue.Enqueue(neighbor);
                                currentComponent.Add(neighbor);
                            }
                        }
                    }
                }

                allComponents.Add(currentComponent);
            }
        }

        return allComponents;
    }
}

public class Program
{
    public static void Main(string[] args)
    {
        Console.WriteLine("Bfs TEST -----> start");

        Console.WriteLine("\nnew");
        var graphData = new GraphData();
        Console.WriteLine($"  現在のデータ: {string.Join(", ", graphData.Get())}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList1 = new[] 
        {
            ("A", "B", 4), ("B", "C", 3), ("B", "D", 2), 
            ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)
        };
        foreach (var input in inputList1)
        {
            Console.WriteLine($"  入力値: {input}");
            var output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {string.Join(", ", graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var output1 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {string.Join(", ", output1.Select(c => string.Join(",", c)))}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList2 = new[] 
        {
            ("A", "B", 4), ("C", "D", 4), ("E", "F", 1), ("F", "G", 1)
        };
        foreach (var input in inputList2)
        {
            Console.WriteLine($"  入力値: {input}");
            var output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {string.Join(", ", graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var output2 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {string.Join(", ", output2.Select(c => string.Join(",", c)))}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList3 = new[] 
        {
            ("A", "B", 4), ("B", "C", 3), ("D", "E", 5)
        };
        foreach (var input in inputList3)
        {
            Console.WriteLine($"  入力値: {input}");
            var output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {string.Join(", ", graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var output3 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {string.Join(", ", output3.Select(c => string.Join(",", c)))}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList4 = new (string, string, int)[] { };
        foreach (var input in inputList4)
        {
            Console.WriteLine($"  入力値: {input}");
            var output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {string.Join(", ", graphData.Get())}");
        Console.WriteLine("\nget_connected_components");
        var output4 = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {string.Join(", ", output4.Select(c => string.Join(",", c)))}");

        Console.WriteLine("Bfs TEST <----- end");
    }
}
