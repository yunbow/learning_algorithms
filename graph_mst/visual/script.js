// グラフデータの初期化
const graphData = {
    nodes: [
        { id: 'A', x: 150, y: 100 },
        { id: 'B', x: 350, y: 80 },
        { id: 'C', x: 500, y: 150 },
        { id: 'D', x: 450, y: 270 },
        { id: 'E', x: 250, y: 300 },
        { id: 'F', x: 80, y: 220 }
    ],
    edges: [
        { from: 'A', to: 'B', weight: 4 },
        { from: 'A', to: 'F', weight: 2 },
        { from: 'B', to: 'C', weight: 3 },
        { from: 'B', to: 'E', weight: 6 },
        { from: 'C', to: 'D', weight: 5 },
        { from: 'D', to: 'E', weight: 1 },
        { from: 'E', to: 'F', weight: 8 },
        { from: 'F', to: 'B', weight: 7 },
        { from: 'A', to: 'E', weight: 9 }
    ]
};

// グローバル変数
let animationInProgress = false;
let animationInterval;
let currentStep = 0;
let algorithm = 'kruskal';
let animationSteps = [];
let mst = [];

// DOMが読み込まれた後に実行
document.addEventListener('DOMContentLoaded', () => {
    // グラフの初期描画
    drawGraph();
    
    // イベントリスナーの設定
    document.getElementById('algorithm-select').addEventListener('change', (e) => {
        algorithm = e.target.value;
        resetAnimation();
    });
    
    document.getElementById('start-btn').addEventListener('click', startAnimation);
    document.getElementById('clear-btn').addEventListener('click', resetAnimation);
});

// グラフの描画関数
function drawGraph() {
    const graph = document.getElementById('graph');
    graph.innerHTML = '';
    
    // エッジを描画
    graphData.edges.forEach(edge => {
        const fromNode = graphData.nodes.find(n => n.id === edge.from);
        const toNode = graphData.nodes.find(n => n.id === edge.to);
        
        const length = Math.sqrt(Math.pow(toNode.x - fromNode.x, 2) + Math.pow(toNode.y - fromNode.y, 2));
        const angle = Math.atan2(toNode.y - fromNode.y, toNode.x - fromNode.x);
        
        const edgeElem = document.createElement('div');
        edgeElem.className = 'edge';
        edgeElem.id = `edge-${edge.from}-${edge.to}`;
        edgeElem.style.width = `${length}px`;
        edgeElem.style.left = `${fromNode.x + 20}px`;
        edgeElem.style.top = `${fromNode.y + 20}px`;
        edgeElem.style.transform = `rotate(${angle}rad)`;
        
        const weightElem = document.createElement('div');
        weightElem.className = 'edge-weight';
        weightElem.textContent = edge.weight;
        weightElem.style.left = `${(fromNode.x + toNode.x) / 2}px`;
        weightElem.style.top = `${(fromNode.y + toNode.y) / 2}px`;
        
        graph.appendChild(edgeElem);
        graph.appendChild(weightElem);
    });
    
    // ノードを描画
    graphData.nodes.forEach(node => {
        const nodeElem = document.createElement('div');
        nodeElem.className = 'node';
        nodeElem.id = `node-${node.id}`;
        nodeElem.textContent = node.id;
        nodeElem.style.left = `${node.x}px`;
        nodeElem.style.top = `${node.y}px`;
        
        graph.appendChild(nodeElem);
    });
}

// アニメーション開始関数
function startAnimation() {
    if (animationInProgress) return;
    
    resetAnimation();
    animationInProgress = true;
    
    // 選択されたアルゴリズムに基づいてアニメーションステップを準備
    if (algorithm === 'kruskal') {
        prepareKruskalSteps();
    } else {
        preparePrimSteps();
    }
    
    // ステップごとのアニメーション実行
    currentStep = 0;
    updateStepInfo();
    
    animationInterval = setInterval(() => {
        if (currentStep >= animationSteps.length) {
            clearInterval(animationInterval);
            animationInProgress = false;
            return;
        }
        
        const step = animationSteps[currentStep];
        executeStep(step);
        currentStep++;
        updateStepInfo();
        
        if (currentStep >= animationSteps.length) {
            clearInterval(animationInterval);
            animationInProgress = false;
        }
    }, 1500);
}

// クラスカル法のアニメーションステップ準備
function prepareKruskalSteps() {
    animationSteps = [];
    
    // エッジを重みでソート
    const sortedEdges = [...graphData.edges].sort((a, b) => a.weight - b.weight);
    
    // Union-Find用の集合
    const sets = {};
    graphData.nodes.forEach(node => {
        sets[node.id] = node.id;
    });
    
    // ステップ1: 初期状態
    animationSteps.push({
        type: 'init',
        message: 'クラスカル法: すべてのエッジを重みの昇順にソートします。'
    });
    
    // 各エッジについて処理
    sortedEdges.forEach(edge => {
        // ステップ2: エッジの考慮
        animationSteps.push({
            type: 'consider',
            edge: edge,
            message: `エッジ ${edge.from}-${edge.to} (重み: ${edge.weight}) を考慮します。`
        });
        
        // 2つのノードの代表元を見つける
        const findSet = (id) => {
            if (sets[id] !== id) {
                sets[id] = findSet(sets[id]);
            }
            return sets[id];
        };
        
        const fromSet = findSet(edge.from);
        const toSet = findSet(edge.to);
        
        // サイクルが形成されないか確認
        if (fromSet !== toSet) {
            // ステップ3: エッジの追加
            animationSteps.push({
                type: 'include',
                edge: edge,
                message: `エッジ ${edge.from}-${edge.to} を最小全域木に追加します。サイクルを形成しません。`
            });
            
            // 集合をマージ
            sets[fromSet] = toSet;
            mst.push(edge);
        } else {
            // ステップ4: エッジの除外
            animationSteps.push({
                type: 'reject',
                edge: edge,
                message: `エッジ ${edge.from}-${edge.to} はサイクルを形成するため除外します。`
            });
        }
    });
    
    // 最終ステップ
    animationSteps.push({
        type: 'complete',
        message: '最小全域木の構築が完了しました。緑色のエッジが最小全域木を形成します。'
    });
}

// プリム法のアニメーションステップ準備
function preparePrimSteps() {
    animationSteps = [];
    
    // 訪問済みノード集合
    const visited = new Set();
    // 開始ノード
    const startNode = graphData.nodes[0].id;
    
    // ステップ1: 初期状態
    animationSteps.push({
        type: 'init',
        message: `プリム法: スタートノード ${startNode} から最小全域木の構築を開始します。`
    });
    
    // 開始ノードを訪問済みに追加
    animationSteps.push({
        type: 'visit',
        node: startNode,
        message: `ノード ${startNode} を訪問済みに追加します。`
    });
    visited.add(startNode);
    
    // 未訪問ノードがある限り繰り返す
    while (visited.size < graphData.nodes.length) {
        let minEdge = null;
        let minWeight = Infinity;
        
        // 訪問済みノードから未訪問ノードへの最小重みエッジを探す
        visited.forEach(nodeId => {
            // このノードから出るすべてのエッジを取得
            const outgoingEdges = graphData.edges.filter(e => 
                (e.from === nodeId && !visited.has(e.to)) || 
                (e.to === nodeId && !visited.has(e.from))
            );
            
            // 最小のエッジを見つける
            outgoingEdges.forEach(edge => {
                // エッジの考慮
                const considerEdge = {
                    type: 'consider',
                    edge: edge,
                    message: `エッジ ${edge.from}-${edge.to} (重み: ${edge.weight}) を考慮します。`
                };
                animationSteps.push(considerEdge);
                
                if (edge.weight < minWeight) {
                    minEdge = edge;
                    minWeight = edge.weight;
                }
            });
        });
        
        if (minEdge) {
            // 次のノードを訪問済みに追加
            const nextNode = visited.has(minEdge.from) ? minEdge.to : minEdge.from;
            
            // ステップ: エッジの追加
            animationSteps.push({
                type: 'include',
                edge: minEdge,
                message: `エッジ ${minEdge.from}-${minEdge.to} (重み: ${minEdge.weight}) は現在の最小エッジです。最小全域木に追加します。`
            });
            
            // ステップ: ノードの訪問
            animationSteps.push({
                type: 'visit',
                node: nextNode,
                message: `ノード ${nextNode} を訪問済みに追加します。`
            });
            
            visited.add(nextNode);
            mst.push(minEdge);
        } else {
            // これは理論的にはあり得ないが、安全のため
            break;
        }
    }
    
    // 最終ステップ
    animationSteps.push({
        type: 'complete',
        message: '最小全域木の構築が完了しました。緑色のエッジが最小全域木を形成します。'
    });
}

// 各ステップの実行
function executeStep(step) {
    switch (step.type) {
        case 'init':
            // 初期化ステップ
            break;
            
        case 'consider':
            // エッジを考慮中
            const considerEdgeElem = document.getElementById(`edge-${step.edge.from}-${step.edge.to}`) || 
                                    document.getElementById(`edge-${step.edge.to}-${step.edge.from}`);
            if (considerEdgeElem) {
                considerEdgeElem.classList.add('considering');
                considerEdgeElem.classList.remove('rejected');
                considerEdgeElem.classList.remove('included');
            }
            break;
            
        case 'include':
            // エッジを含める
            const includeEdgeElem = document.getElementById(`edge-${step.edge.from}-${step.edge.to}`) || 
                                   document.getElementById(`edge-${step.edge.to}-${step.edge.from}`);
            if (includeEdgeElem) {
                includeEdgeElem.classList.remove('considering');
                includeEdgeElem.classList.add('included');
            }
            
            // ノードをアクティブに
            const fromNodeElem = document.getElementById(`node-${step.edge.from}`);
            const toNodeElem = document.getElementById(`node-${step.edge.to}`);
            if (fromNodeElem) fromNodeElem.classList.add('active');
            if (toNodeElem) toNodeElem.classList.add('active');
            break;
            
        case 'reject':
            // エッジを除外
            const rejectEdgeElem = document.getElementById(`edge-${step.edge.from}-${step.edge.to}`) || 
                                  document.getElementById(`edge-${step.edge.to}-${step.edge.from}`);
            if (rejectEdgeElem) {
                rejectEdgeElem.classList.remove('considering');
                rejectEdgeElem.classList.add('rejected');
            }
            break;
            
        case 'visit':
            // ノードを訪問
            const nodeElem = document.getElementById(`node-${step.node}`);
            if (nodeElem) {
                nodeElem.classList.add('active');
            }
            break;
            
        case 'complete':
            // 完了ステップ
            break;
    }
}

// ステップ情報の更新
function updateStepInfo() {
    const stepCounter = document.getElementById('step-counter');
    const stepInfo = document.getElementById('step-info');
    
    stepCounter.textContent = `ステップ: ${currentStep}`;
    
    if (currentStep < animationSteps.length) {
        stepInfo.textContent = animationSteps[currentStep].message;
    } else {
        stepInfo.textContent = '最小全域木の構築が完了しました。';
    }
}

// アニメーションのリセット
function resetAnimation() {
    if (animationInterval) {
        clearInterval(animationInterval);
    }
    
    animationInProgress = false;
    currentStep = 0;
    mst = [];
    animationSteps = [];
    
    // 要素をリセット
    const edges = document.querySelectorAll('.edge');
    edges.forEach(edge => {
        edge.classList.remove('considering', 'included', 'rejected');
    });
    
    const nodes = document.querySelectorAll('.node');
    nodes.forEach(node => {
        node.classList.remove('active');
    });
    
    // 情報をリセット
    document.getElementById('step-counter').textContent = 'ステップ: 0';
    document.getElementById('step-info').textContent = 'アルゴリズムを選択して実行ボタンを押してください。';
}