document.addEventListener('DOMContentLoaded', function() {
    // グラフの構築
    const graph = {
        nodes: [
            { id: 0, x: 100, y: 250, name: 'S', heuristic: 8 },  // スタートノード
            { id: 1, x: 250, y: 100, name: 'A', heuristic: 6 },
            { id: 2, x: 250, y: 400, name: 'B', heuristic: 7 },
            { id: 3, x: 400, y: 150, name: 'C', heuristic: 4 },
            { id: 4, x: 400, y: 350, name: 'D', heuristic: 3 },
            { id: 5, x: 550, y: 100, name: 'E', heuristic: 2 },
            { id: 6, x: 550, y: 400, name: 'F', heuristic: 2 },
            { id: 7, x: 700, y: 250, name: 'G', heuristic: 0 }   // ゴールノード
        ],
        edges: [
            { from: 0, to: 1, cost: 2 },
            { from: 0, to: 2, cost: 3 },
            { from: 1, to: 3, cost: 3 },
            { from: 2, to: 3, cost: 5 },
            { from: 2, to: 4, cost: 2 },
            { from: 3, to: 5, cost: 3 },
            { from: 3, to: 4, cost: 4 },
            { from: 4, to: 6, cost: 4 },
            { from: 5, to: 7, cost: 4 },
            { from: 6, to: 7, cost: 3 }
        ]
    };

    // A*アルゴリズムの状態
    let openSet = [];
    let closedSet = [];
    let startNode = graph.nodes[0];
    let goalNode = graph.nodes[7];
    let cameFrom = new Map();
    let gScore = new Map();
    let fScore = new Map();
    let currentNode = null;
    let path = [];
    let algorithmSteps = [];
    let currentStep = 0;
    let isRunning = false;
    let isPaused = false;
    let animationSpeed = 5;
    let animationInterval = null;

    // DOM要素
    const runButton = document.getElementById('run-animation');
    const stepButton = document.getElementById('step-button');
    const clearButton = document.getElementById('clear-button');
    const speedSlider = document.getElementById('speed');
    const speedValue = document.getElementById('speed-value');
    const progressBar = document.getElementById('progress-bar');
    const description = document.getElementById('description');
    const exploredCount = document.getElementById('explored-count');
    const pathLength = document.getElementById('path-length');
    const svg = document.getElementById('graph');

    // グラフを描画
    function drawGraph() {
        svg.innerHTML = '';

        // エッジを描画
        graph.edges.forEach(edge => {
            const fromNode = graph.nodes[edge.from];
            const toNode = graph.nodes[edge.to];
            const edgeClass = path.some(p => p.from === edge.from && p.to === edge.to || p.from === edge.to && p.to === edge.from) ? 'edge-path' : 
                           (closedSet.includes(fromNode) && closedSet.includes(toNode)) ? 'edge-confirmed' : 
                           (openSet.includes(fromNode) || openSet.includes(toNode)) ? 'edge-running' : 'edge-unconfirmed';
            
            // エッジ線
            const line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
            line.setAttribute('x1', fromNode.x);
            line.setAttribute('y1', fromNode.y);
            line.setAttribute('x2', toNode.x);
            line.setAttribute('y2', toNode.y);
            line.setAttribute('class', edgeClass);
            svg.appendChild(line);

            // エッジのコスト表示
            const costX = (fromNode.x + toNode.x) / 2;
            const costY = (fromNode.y + toNode.y) / 2;
            const cost = document.createElementNS('http://www.w3.org/2000/svg', 'text');
            cost.setAttribute('x', costX);
            cost.setAttribute('y', costY);
            cost.setAttribute('class', 'edge-cost');
            cost.textContent = edge.cost;
            svg.appendChild(cost);
        });

        // ノードを描画
        graph.nodes.forEach(node => {
            const nodeGroup = document.createElementNS('http://www.w3.org/2000/svg', 'g');
            nodeGroup.setAttribute('class', 'node');

            let nodeClass = '';
            if (node === startNode) {
                nodeClass = 'node-start';
            } else if (node === goalNode) {
                nodeClass = 'node-goal';
            } else if (path.some(p => p.from === node.id || p.to === node.id)) {
                nodeClass = 'node-selected';
            } else if (closedSet.includes(node)) {
                nodeClass = 'node-confirmed';
            } else if (openSet.includes(node)) {
                nodeClass = 'node-running';
            } else {
                nodeClass = 'node-unconfirmed';
            }

            // ノード円
            const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
            circle.setAttribute('cx', node.x);
            circle.setAttribute('cy', node.y);
            circle.setAttribute('r', 20);
            circle.setAttribute('class', nodeClass);
            nodeGroup.appendChild(circle);

            // ノード名
            const text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
            text.setAttribute('x', node.x);
            text.setAttribute('y', node.y);
            text.setAttribute('class', 'node-name');
            text.textContent = node.name;
            nodeGroup.appendChild(text);

            // ヒューリスティック値
            if (node !== goalNode) {
                const heuristicText = document.createElementNS('http://www.w3.org/2000/svg', 'text');
                heuristicText.setAttribute('x', node.x);
                heuristicText.setAttribute('y', node.y + 30);
                heuristicText.setAttribute('class', 'node-heuristic');
                heuristicText.textContent = `h=${node.heuristic}`;
                nodeGroup.appendChild(heuristicText);
            }

            // F値（f = g + h）を表示
            if (fScore.has(node.id)) {
                const gValue = gScore.get(node.id);
                const fValue = fScore.get(node.id);
                const fScoreText = document.createElementNS('http://www.w3.org/2000/svg', 'text');
                fScoreText.setAttribute('x', node.x);
                fScoreText.setAttribute('y', node.y - 30);
                fScoreText.setAttribute('class', 'node-f-score');
                fScoreText.textContent = `g=${gValue}, f=${fValue}`;
                nodeGroup.appendChild(fScoreText);
            }

            svg.appendChild(nodeGroup);
        });
    }

    // A*アルゴリズムのステップを記録
    function prepareAStarAlgorithm() {
        algorithmSteps = [];
        openSet = [startNode];
        closedSet = [];
        cameFrom = new Map();
        
        // 初期化: gScore[start] = 0, fScore[start] = heuristic[start]
        gScore = new Map();
        fScore = new Map();
        graph.nodes.forEach(node => {
            gScore.set(node.id, Infinity);
            fScore.set(node.id, Infinity);
        });
        gScore.set(startNode.id, 0);
        fScore.set(startNode.id, startNode.heuristic);

        path = [];
        currentNode = null;

        // 初期状態を記録
        algorithmSteps.push({
            description: 'アルゴリズムを開始します。スタートノードをオープンセットに追加します。',
            openSet: [...openSet],
            closedSet: [...closedSet],
            currentNode: null,
            path: [],
            gScore: new Map(gScore),
            fScore: new Map(fScore),
            cameFrom: new Map(cameFrom)
        });

        // A*アルゴリズムを実行し、各ステップを記録
        while (openSet.length > 0) {
            // fスコアが最小のノードを選択
            currentNode = openSet.reduce((min, node) => 
                fScore.get(node.id) < fScore.get(min.id) ? node : min, openSet[0]);
            
            // ゴールに到達した場合
            if (currentNode === goalNode) {
                // 最終パスを再構築
                reconstructPath();
                
                algorithmSteps.push({
                    description: 'ゴールに到達しました！最短経路が見つかりました。',
                    openSet: [...openSet],
                    closedSet: [...closedSet],
                    currentNode: currentNode,
                    path: [...path],
                    gScore: new Map(gScore),
                    fScore: new Map(fScore),
                    cameFrom: new Map(cameFrom)
                });
                break;
            }

            // 現在のノードをオープンセットから削除、クローズドセットに追加
            openSet = openSet.filter(node => node !== currentNode);
            closedSet.push(currentNode);

            // 現在のノードの隣接ノードを探索
            const neighbors = getNeighbors(currentNode);
            
            algorithmSteps.push({
                description: `ノード${currentNode.name}を処理しています。このノードをクローズドセットに移動します。`,
                openSet: [...openSet],
                closedSet: [...closedSet],
                currentNode: currentNode,
                path: [],
                gScore: new Map(gScore),
                fScore: new Map(fScore),
                cameFrom: new Map(cameFrom)
            });

            neighbors.forEach(neighbor => {
                // 隣接ノードがクローズドセットにある場合はスキップ
                if (closedSet.includes(neighbor.node)) {
                    return;
                }

                // 現在のノードから隣接ノードへの暫定的なgスコア
                const tentativeGScore = gScore.get(currentNode.id) + neighbor.cost;

                // より良い経路が見つかった場合
                if (tentativeGScore < gScore.get(neighbor.node.id)) {
                    // この経路を記録
                    cameFrom.set(neighbor.node.id, { node: currentNode, edge: neighbor.edge });
                    gScore.set(neighbor.node.id, tentativeGScore);
                    fScore.set(neighbor.node.id, tentativeGScore + neighbor.node.heuristic);

                    // 隣接ノードがオープンセットにない場合は追加
                    if (!openSet.includes(neighbor.node)) {
                        openSet.push(neighbor.node);
                        
                        algorithmSteps.push({
                            description: `ノード${neighbor.node.name}をオープンセットに追加します。g(${neighbor.node.name})=${tentativeGScore}, f(${neighbor.node.name})=${tentativeGScore + neighbor.node.heuristic}`,
                            openSet: [...openSet],
                            closedSet: [...closedSet],
                            currentNode: currentNode,
                            path: [],
                            gScore: new Map(gScore),
                            fScore: new Map(fScore),
                            cameFrom: new Map(cameFrom)
                        });
                    } else {
                        algorithmSteps.push({
                            description: `ノード${neighbor.node.name}への新しい良い経路が見つかりました。g(${neighbor.node.name})=${tentativeGScore}, f(${neighbor.node.name})=${tentativeGScore + neighbor.node.heuristic}`,
                            openSet: [...openSet],
                            closedSet: [...closedSet],
                            currentNode: currentNode,
                            path: [],
                            gScore: new Map(gScore),
                            fScore: new Map(fScore),
                            cameFrom: new Map(cameFrom)
                        });
                    }
                }
            });

            // オープンセットが空の場合
            if (openSet.length === 0) {
                algorithmSteps.push({
                    description: 'オープンセットが空になりました。経路が見つかりませんでした。',
                    openSet: [],
                    closedSet: [...closedSet],
                    currentNode: null,
                    path: [],
                    gScore: new Map(gScore),
                    fScore: new Map(fScore),
                    cameFrom: new Map(cameFrom)
                });
            }
        }
    }

    // 最短経路を再構築
    function reconstructPath() {
        path = [];
        let current = goalNode.id;
        
        while (current !== startNode.id) {
            const prev = cameFrom.get(current);
            path.push({
                from: prev.node.id, 
                to: current,
                node: graph.nodes[current]
            });
            current = prev.node.id;
        }
        
        path.reverse();
    }

    // ノードの隣接ノードとエッジを取得
    function getNeighbors(node) {
        const neighbors = [];
        graph.edges.forEach(edge => {
            if (edge.from === node.id) {
                neighbors.push({
                    node: graph.nodes[edge.to],
                    cost: edge.cost,
                    edge: edge
                });
            } else if (edge.to === node.id) {
                // 双方向グラフの場合
                neighbors.push({
                    node: graph.nodes[edge.from],
                    cost: edge.cost,
                    edge: edge
                });
            }
        });
        return neighbors;
    }

    // アルゴリズムのステップを実行
    function executeStep() {
        if (currentStep < algorithmSteps.length) {
            const step = algorithmSteps[currentStep];
            
            // 状態を更新
            openSet = step.openSet;
            closedSet = step.closedSet;
            currentNode = step.currentNode;
            path = step.path;
            gScore = new Map(step.gScore);
            fScore = new Map(step.fScore);
            cameFrom = new Map(step.cameFrom);
            
            // 説明を更新
            description.textContent = step.description;
            
            // 探索済みノード数と最短経路長を更新
            exploredCount.textContent = closedSet.length;
            if (path.length > 0) {
                let totalCost = 0;
                path.forEach(p => {
                    const edge = graph.edges.find(e => 
                        (e.from === p.from && e.to === p.to) || 
                        (e.from === p.to && e.to === p.from));
                    if (edge) {
                        totalCost += edge.cost;
                    }
                });
                pathLength.textContent = totalCost;
            } else {
                pathLength.textContent = '-';
            }
            
            // グラフを更新
            drawGraph();
            
            // プログレスバーを更新
            progressBar.style.width = `${(currentStep + 1) / algorithmSteps.length * 100}%`;
            
            currentStep++;
            
            return currentStep >= algorithmSteps.length;
        }
        return true;
    }

    // アニメーションの実行
    function runAnimation() {
        if (isPaused) {
            // 一時停止状態から再開
            isPaused = false;
            runButton.textContent = '一時停止';
            animationInterval = setInterval(() => {
                const isFinished = executeStep();
                if (isFinished) {
                    clearInterval(animationInterval);
                    isRunning = false;
                    runButton.textContent = 'アニメーション実行';
                }
            }, 1000 / animationSpeed);
        } else if (!isRunning) {
            // 新しいアニメーションを開始
            isRunning = true;
            runButton.textContent = '一時停止';
            if (currentStep === algorithmSteps.length) {
                // 終了していたら最初から
                resetAlgorithm();
            }
            animationInterval = setInterval(() => {
                const isFinished = executeStep();
                if (isFinished) {
                    clearInterval(animationInterval);
                    isRunning = false;
                    runButton.textContent = 'アニメーション実行';
                }
            }, 1000 / animationSpeed);
        } else {
            // 実行中なら一時停止
            clearInterval(animationInterval);
            isPaused = true;
            isRunning = false;
            runButton.textContent = 'アニメーション再開';
        }
    }

    // ステップ実行
    function stepExecution() {
        if (isRunning) {
            clearInterval(animationInterval);
            isRunning = false;
            isPaused = true;
            runButton.textContent = 'アニメーション再開';
        }
        
        if (currentStep === algorithmSteps.length) {
            resetAlgorithm();
        }
        
        executeStep();
    }

    // アルゴリズムをリセット
    function resetAlgorithm() {
        if (isRunning) {
            clearInterval(animationInterval);
            isRunning = false;
            isPaused = false;
        }
        
        currentStep = 0;
        openSet = [];
        closedSet = [];
        path = [];
        currentNode = null;
        
        description.textContent = 'A*アルゴリズムを開始するには、「アニメーション実行」または「ステップ実行」ボタンをクリックしてください。';
        exploredCount.textContent = '0';
        pathLength.textContent = '-';
        progressBar.style.width = '0%';
        
        runButton.textContent = 'アニメーション実行';
        
        drawGraph();
    }

    // イベントリスナーの設定
    runButton.addEventListener('click', runAnimation);
    stepButton.addEventListener('click', stepExecution);
    clearButton.addEventListener('click', resetAlgorithm);
    
    speedSlider.addEventListener('input', function() {
        animationSpeed = parseInt(this.value);
        speedValue.textContent = this.value;
        
        if (isRunning && !isPaused) {
            clearInterval(animationInterval);
            animationInterval = setInterval(() => {
                const isFinished = executeStep();
                if (isFinished) {
                    clearInterval(animationInterval);
                    isRunning = false;
                    runButton.textContent = 'アニメーション実行';
                }
            }, 1000 / animationSpeed);
        }
    });

    // 初期化
    prepareAStarAlgorithm();
    drawGraph();
});