// Dashboard logic for PC Build Evaluation & Comparison
const BASE_URL = 'http://localhost:8080/identity/api/v1/builds';

/**
 * Handle Single Build Evaluation
 */
async function evaluateBuild(requestData) {
    try {
        const response = await fetch(`${BASE_URL}/evaluate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });
        const data = await response.json();
        if (data.code === 1000) {
            updateEvaluationUI(data.result);
        }
    } catch (err) {
        console.error("Evaluation error:", err);
        // Fallback or demo behavior
        updateEvaluationUI({
            totalScore: 28540,
            tier: "S-Tier (4K Gaming)",
            breakdown: { cpuScore: 45000, gpuScore: 35000, ramBonus: 1500, storageBonus: 1000 },
            radarChartData: { gaming: 95, multitasking: 88, storage: 90, efficiency: 80 },
            gameCompatibility: [
                { gameName: "Cyberpunk 2077", isPlayable: true, status: "Ultra RT" },
                { gameName: "Red Dead Redemption 2", isPlayable: true, status: "Ultra" },
                { gameName: "God of War Ragnarok", isPlayable: true, status: "Ultra" },
                { gameName: "Apex Legends", isPlayable: true, status: "Max FPS" },
                { gameName: "Valorant", isPlayable: true, status: "Esports" }
            ]
        });
    }
}

function updateEvaluationUI(result) {
    document.getElementById('total-score').innerText = result.totalScore.toLocaleString();
    const tierBadge = document.getElementById('tier-badge');
    tierBadge.innerText = result.tier;
    tierBadge.className = 'tier-badge ' + (result.tier.includes('S') ? 'tier-s' : (result.tier.includes('A') ? 'tier-a' : 'tier-b'));

    document.getElementById('cpu-score-val').innerText = result.breakdown.cpuScore.toLocaleString();
    document.getElementById('cpu-bar').style.width = (result.breakdown.cpuScore / 500) + '%';
    
    document.getElementById('gpu-score-val').innerText = result.breakdown.gpuScore.toLocaleString();
    document.getElementById('gpu-bar').style.width = (result.breakdown.gpuScore / 400) + '%';

    document.getElementById('ram-bonus').innerText = (result.breakdown.ramBonus >= 0 ? '+' : '') + result.breakdown.ramBonus;
    document.getElementById('storage-bonus').innerText = (result.breakdown.storageBonus >= 0 ? '+' : '') + result.breakdown.storageBonus;

    updateRadarChart(result.radarChartData);
    updateGameList(result.gameCompatibility);
}

let radarChart; 
function updateRadarChart(labelsData) {
    const ctx = document.getElementById('radarChart').getContext('2d');
    if (radarChart) radarChart.destroy();

    radarChart = new Chart(ctx, {
        type: 'radar',
        data: {
            labels: Object.keys(labelsData).map(k => k.toUpperCase()),
            datasets: [{
                label: 'System Performance',
                data: Object.values(labelsData),
                fill: true,
                backgroundColor: 'rgba(56, 189, 248, 0.2)',
                borderColor: '#38bdf8',
                pointBackgroundColor: '#38bdf8',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: '#38bdf8'
            }]
        },
        options: {
            scales: {
                r: {
                    angleLines: { color: 'rgba(255, 255, 255, 0.1)' },
                    grid: { color: 'rgba(255, 255, 255, 0.1)' },
                    pointLabels: { color: '#94a3b8' },
                    ticks: { display: false, max: 100 }
                }
            },
            plugins: { legend: { display: false } }
        }
    });
}

function updateGameList(games) {
    const list = document.getElementById('game-list');
    list.innerHTML = '';
    games.forEach(g => {
        const item = document.createElement('div');
        item.className = 'game-card ' + (g.isPlayable ? 'playable' : 'unplayable');
        item.innerHTML = `
            <div>
                <div style="font-weight: 700;">${g.gameName}</div>
                <div style="font-size: 0.75rem; color: var(--text-dim);">${g.status}</div>
            </div>
            <div style="font-size: 0.75rem; font-weight: 800; color: ${g.isPlayable ? 'var(--success)' : 'var(--error)'};">
                ${g.isPlayable ? 'READY' : 'X'}
            </div>
        `;
        list.appendChild(item);
    });
}
