// C#
// グラフの連結成分: DFS

using System;
using System.Collections.Generic;
using System.Linq;

public class GraphData
{
    private Dictionary<string, List<(string Neighbor, int Weight)>> _data;

    public GraphData()
    {
        _data = new Dictionary<string, List<(string, int)>>();
    }

    public Dictionary<string, List<(string, int)>> Get()
    {
        return _data;
    }

    public List<string> GetVertices()
    {
        return _data.Keys.ToList();
    }

    public List<(string U, string V, int Weight)> GetEdges()
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

    public List<(string Neighbor, int Weight)>? GetNeighbors(string vertex)
    {
        return _data.TryGetValue(vertex, out var neighbors) ? neighbors : null;
    }

    public int? GetEdgeWeight(string vertex1, string vertex2)
    {
        if (_data.TryGetValue(vertex1, out var neighbors))
        {
            var edge = neighbors.FirstOrDefault(n => n.Neighbor == vertex2);
            return edge.Neighbor != null ? edge.Weight : (int?)null;
        }
        return null;
    }

    public List<(string Neighbor, int Weight)>? GetVertice(string vertex)
    {
        if (_data.TryGetValue(vertex, out var value))
        {
            return value;
        }
        Console.WriteLine($"ERROR: {vertex}は範囲外です");
        return null;
    }

    public bool GetEdge(string vertex1, string vertex2)
    {
        return _data.TryGetValue(vertex1, out var neighbors) && 
               neighbors.Any(n => n.Neighbor == vertex2);
    }

    public bool AddVertex(string vertex)
    {
        if (!_data.ContainsKey(vertex))
        {
            _data[vertex] = new List<(string, int)>();
        }
        return true;
    }

    public bool AddEdge(string vertex1, string vertex2, int weight)
    {
        AddVertex(vertex1);
        AddVertex(vertex2);

        // Ensure no duplicate edges for vertex1 -> vertex2
        var existsV1V2 = _data[vertex1].FindIndex(n => n.Neighbor == vertex2);
        if (existsV1V2 != -1)
        {
            _data[vertex1][existsV1V2] = (vertex2, weight);
        }
        else
        {
            _data[vertex1].Add((vertex2, weight));
        }

        // Ensure no duplicate edges for vertex2 -> vertex1
        var existsV2V1 = _data[vertex2].FindIndex(n => n.Neighbor == vertex1);
        if (existsV2V1 != -1)
        {
            _data[vertex2][existsV2V1] = (vertex1, weight);
        }
        else
        {
            _data[vertex2].Add((vertex1, weight));
        }

        return true;
    }

    public bool RemoveVertex(string vertex)
    {
        if (_data.ContainsKey(vertex))
        {
            // Remove references to this vertex from all other vertices
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
            
            // Remove edge from vertex1 to vertex2
            int originalLenV1 = _data[vertex1].Count;
            _data[vertex1] = _data[vertex1].Where(n => n.Neighbor != vertex2).ToList();
            if (_data[vertex1].Count < originalLenV1)
                removed = true;

            // Remove edge from vertex2 to vertex1
            int originalLenV2 = _data[vertex2].Count;
            _data[vertex2] = _data[vertex2].Where(n => n.Neighbor != vertex1).ToList();
            if (_data[vertex2].Count < originalLenV2)
                removed = true;

            return removed;
        }
        
        Console.WriteLine($"ERROR: {vertex1} または {vertex2} は範囲外です");
        return false;
    }

    public bool IsEmpty()
    {
        return _data.Count == 0;
    }

    public int Size()
    {
        return _data.Count;
    }

    public bool Clear()
    {
        _data.Clear();
        return true;
    }

    private void Dfs(string vertex, HashSet<string> visited, List<string> currentComponent)
    {
        visited.Add(vertex);
        currentComponent.Add(vertex);

        if (_data.TryGetValue(vertex, out var neighbors))
        {
            foreach (var (neighborVertex, _) in neighbors)
            {
                if (!visited.Contains(neighborVertex))
                {
                    Dfs(neighborVertex, visited, currentComponent);
                }
            }
        }
    }

    public List<List<string>> GetConnectedComponents()
    {
        var visited = new HashSet<string>();
        var connectedComponents = new List<List<string>>();

        foreach (var vertex in GetVertices())
        {
            if (!visited.Contains(vertex))
            {
                var currentComponent = new List<string>();
                Dfs(vertex, visited, currentComponent);
                connectedComponents.Add(currentComponent);
            }
        }

        return connectedComponents;
    }

    public static void Main(string[] args)
    {
        Console.WriteLine("Dfs TEST -----> start");

        Console.WriteLine("\nnew");
        var graphData = new GraphData();
        Console.WriteLine($"  現在のデータ: {string.Join(", ", graphData.Get())}");

        Console.WriteLine("\nadd_edge");
        graphData.Clear();
        var inputList = new[]
        {
            ("A", "B", 4), ("B", "C", 3), ("B", "D", 2), 
            ("D", "A", 1), ("A", "C", 2), ("B", "D", 2)
        };
        
        foreach (var input in inputList)
        {
            Console.WriteLine($"  入力値: {input}");
            var output = graphData.AddEdge(input.Item1, input.Item2, input.Item3);
            Console.WriteLine($"  出力値: {output}");
        }
        Console.WriteLine($"  現在のデータ: {string.Join(", ", graphData.Get())}");
        
        Console.WriteLine("\nget_connected_components");
        var connectedComponents = graphData.GetConnectedComponents();
        Console.WriteLine($"  連結成分: {string.Join(", ", connectedComponents.Select(c => string.Join(",", c)))}");

        // Similar code blocks for other test cases can be added here
        Console.WriteLine("Dfs TEST <----- end");
    }
}
