const card = document.querySelector('.main-container');
const bgImage = document.querySelector('.bg-image');
const rightPanel = document.querySelector('.right-panel');
const leftPanel = document.querySelector('.left-panel');
const trendList = document.getElementById('trend-list');
let isDragging = false;
let offsetX, offsetY;

card.addEventListener('mousedown', (e) => {
    isDragging = true;
    offsetX = e.clientX - card.getBoundingClientRect().left;
    offsetY = e.clientY - card.getBoundingClientRect().top;
    card.style.cursor = 'grabbing';
    card.style.position = 'fixed';
    card.style.zIndex = '20';
});

document.addEventListener('mousemove', (e) => {
    if (!isDragging) return;
    card.style.left = (e.clientX - offsetX) + 'px';
    card.style.top = (e.clientY - offsetY) + 'px';
});

document.addEventListener('mouseup', () => {
    if (!isDragging) return;
    isDragging = false;
    card.style.cursor = 'grab';
    card.style.zIndex = '1';
});

function positionPanels() {
    const bannerHeight = document.querySelector('.top-banner').getBoundingClientRect().height;
    const vw = window.innerWidth;
    const vh = window.innerHeight;

    const imgNaturalRatio = bgImage.naturalWidth / bgImage.naturalHeight;
    const viewportRatio = vw / vh;

    let imgRenderedWidth;
    if (imgNaturalRatio > viewportRatio) {
        imgRenderedWidth = vw;
    } else {
        imgRenderedWidth = vh * imgNaturalRatio;
    }

    const imageLeftEdge = (vw / 2) - (imgRenderedWidth / 2);
    const imageRightEdge = (vw / 2) + (imgRenderedWidth / 2);

    const topStr = bannerHeight + 'px';
    const bottomStr = bannerHeight + 'px';

    rightPanel.style.width = (vw - imageRightEdge) + 'px';
    rightPanel.style.top = topStr;
    rightPanel.style.bottom = bottomStr;

    leftPanel.style.width = imageLeftEdge + 'px';
    leftPanel.style.top = topStr;
    leftPanel.style.bottom = bottomStr;
}

bgImage.addEventListener('load', positionPanels);
window.addEventListener('resize', positionPanels);
if (bgImage.complete) positionPanels();

// Poll /api/trends every 60 seconds and refresh the right panel
function fetchAndRenderTrends() {
    fetch('/api/trends?limit=5')
        .then(res => res.json())
        .then(trends => {
            if (!trendList) return;
            if (trends.length === 0) {
                trendList.innerHTML = '<div class="trend-empty">Fetching trends…</div>';
                return;
            }
            trendList.innerHTML = trends.map(t => `
                <div class="trend-item">
                    <div class="trend-topic">${escapeHtml(t.topic)}</div>
                    <div class="trend-title">${escapeHtml(t.title)}</div>
                    <div class="trend-meta">${escapeHtml(t.publishedAt)} · Bluesky</div>
                </div>
            `).join('');
        })
        .catch(() => {}); // silent fail — keep showing last known state
}

function escapeHtml(str) {
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

setInterval(fetchAndRenderTrends, 60000);
