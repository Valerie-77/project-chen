<template>
    <div id="eduardbodak-container">
        <nav>
            <div class="logo"><span>Site Logo</span></div>
            <div class="menu-btn"><span>Menu</span></div>
        </nav>

        <section class="hero">
            <div class="hero-cards">
                <div class="card" id="hero-card-1">
                    <div class="card-title"><span>Plan</span><span>01</span></div>
                    <div class="card-title"><span>01</span><span>Plan</span></div>
                </div>
                <div class="card" id="hero-card-2">
                    <div class="card-title"><span>Design</span><span>02</span></div>
                    <div class="card-title"><span>02</span><span>Design</span></div>
                </div>
                <div class="card" id="hero-card-3">
                    <div class="card-title"><span>Develop</span><span>03</span></div>
                    <div class="card-title"><span>03</span><span>Develop</span></div>
                </div>
            </div>
        </section>

        <section class="about">
            <h1>Keep scrolling - it gets good</h1>
        </section>

        <section class="services">
            <div class="services-header">
                <h1>Stuff I make so you don't have to</h1>
            </div>
        </section>

        <section class="cards">
            <div class="cards-container">
                <div class="card" id="card-1">
                    <div class="card-wrapper">
                        <div class="flip-card-inner">
                            <div class="flip-card-front">
                                <div class="card-title"><span>Plan</span><span>01</span></div>
                                <div class="card-title"><span>01</span><span>Plan</span></div>
                            </div>
                            <div class="flip-card-back">
                                <div class="card-title"><span>Plan</span><span>01</span></div>
                                <div class="card-copy">
                                    <p>Discovery</p>
                                    <p>Audit</p>
                                    <p>User Flow</p>
                                    <p>Site Map</p>
                                    <p>Personas</p>
                                    <p>Strategy</p>
                                </div>
                                <div class="card-title"><span>01</span><span>Plan</span></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card" id="card-2">
                    <div class="card-wrapper">
                        <div class="flip-card-inner">
                            <div class="flip-card-front">
                                <div class="card-title"><span>Design</span><span>02</span></div>
                                <div class="card-title"><span>02</span><span>Design</span></div>
                            </div>
                            <div class="flip-card-back">
                                <div class="card-title"><span>Design</span><span>02</span></div>
                                <div class="card-copy">
                                    <p>Wireframes</p>
                                    <p>UI Kits</p>
                                    <p>Prototypes</p>
                                    <p>Visual Style</p>
                                    <p>Interaction</p>
                                    <p>Design QA</p>
                                </div>
                                <div class="card-title"><span>02</span><span>Design</span></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card" id="card-3">
                    <div class="card-wrapper">
                        <div class="flip-card-inner">
                            <div class="flip-card-front">
                                <div class="card-title"><span>Develop</span><span>03</span></div>
                                <div class="card-title"><span>03</span><span>Develop</span></div>
                            </div>
                            <div class="flip-card-back">
                                <div class="card-title"><span>Develop</span><span>03</span></div>
                                <div class="card-copy">
                                    <p>HTML/CSS/JS</p>
                                    <p>CMS Build</p>
                                    <p>GSAP Motion</p>
                                    <p>Responsive</p>
                                    <p>Optimization</p>
                                    <p>Launch</p>
                                </div>
                                <div class="card-title"><span>03</span><span>Develop</span></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="outro">
            <h1>The story's not over yet</h1>
        </section>
    </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
onMounted(() => {
    const script = document.createElement('script')
    script.text = `
    gsap.registerPlugin(ScrollTrigger);

    const lenis = new Lenis();
    lenis.on("scroll", ScrollTrigger.update);
    gsap.ticker.add((time) => {
        lenis.raf(time * 1000);
    });
    gsap.ticker.lagSmoothing(0);

    const smoothStep = (p) => p * p * (3 - 2 * p);

    ScrollTrigger.create({
        trigger: ".hero",
        start: "top top",
        end: "75% top",
        scrub: 1,
        onUpdate: (self) => {
            const progress = self.progress;
            const heroCardContainerOpacity = gsap.utils.interpolate(1, 0.5, smoothStep(progress));
            gsap.set(".hero-cards", { opacity: heroCardContainerOpacity });

            ["#hero-card-1", "#hero-card-2", "#hero-card-3"].forEach((cardId, index) => {
                const delay = index * 0.9;
                const cardProgress = gsap.utils.clamp(0, 1, (progress - delay * 0.1) / (1 - delay * 0.1));
                const y = gsap.utils.interpolate("0%", "250%", smoothStep(cardProgress));
                const scale = gsap.utils.interpolate(1, 0.75, smoothStep(cardProgress));
                let x = "0%";
                let rotation = 0;
                if (index === 0) {
                    x = gsap.utils.interpolate("0%", "90%", smoothStep(cardProgress));
                    rotation = gsap.utils.interpolate(0, -15, smoothStep(cardProgress));
                } else if (index === 2) {
                    x = gsap.utils.interpolate("0%", "-90%", smoothStep(cardProgress));
                    rotation = gsap.utils.interpolate(0, 15, smoothStep(cardProgress));
                }
                gsap.set(cardId, { y, scale, x, rotation });
            });
        }
    });

    ScrollTrigger.create({
        trigger: ".services",
        start: "top top",
        end: "+=" + (window.innerHeight * 4) + "px",
        pin: ".services",
        pinSpacing: true,
    });

    ScrollTrigger.create({
        trigger: ".services",
        start: "top top",
        end: "+=" + (window.innerHeight * 4) + "px",
        onLeave: () => {
            const servicesSection = document.querySelector(".services");
            const servicesRect = servicesSection.getBoundingClientRect();
            const servicesTop = servicesRect.top + window.pageYOffset;
            gsap.set(".cards", {
                position: "absolute",
                top: servicesTop,
                left: 0,
                width: "100vw",
                height: "100vh",
            });
        },
        onEnterBack: () => {
            gsap.set(".cards", { position: "fixed", top: 0, left: 0, width: "100vw", height: "100vh" });
        }
    });

    ScrollTrigger.create({
        trigger: ".services",
        start: "top bottom",
        end: "+=" + (window.innerHeight * 4) + "px",
        scrub: 1,
        onUpdate: (self) => {
            const progress = self.progress;
            const headerProgress = gsap.utils.clamp(0, 1, progress * 0.9);
            const headerY = gsap.utils.interpolate("400%", "0%", smoothStep(headerProgress));
            gsap.set(".services-header", { y: headerY });

            ["#card-1", "#card-2", "#card-3"].forEach((cardId, index) => {
                const delay = index * 0.5;
                const cardProgress = gsap.utils.clamp(0, 1, (progress - delay * 0.1) / (0.9 - delay * 0.1));
                const innerCard = document.querySelector(cardId + " .flip-card-inner");

                let y, scale, opacity, x, rotate, rotateY;

                if (cardProgress < 0.4) {
                    const t = cardProgress / 0.4;
                    y = gsap.utils.interpolate("-100%", "50%", smoothStep(t));
                    scale = gsap.utils.interpolate(0.25, 0.75, smoothStep(t));
                    opacity = smoothStep(cardProgress / 0.2);
                    x = index === 0 ? "100%" : index === 1 ? "0%" : "-100%";
                    rotate = index === 0 ? -5 : index === 1 ? 0 : 5;
                    rotateY = 0;
                } else if (cardProgress < 0.6) {
                    const t = (cardProgress - 0.4) / 0.2;
                    y = gsap.utils.interpolate("50%", "0%", smoothStep(t));
                    scale = gsap.utils.interpolate(0.75, 1, smoothStep(t));
                    opacity = 1;
                    x = index === 0 ? "100%" : index === 1 ? "0%" : "-100%";
                    rotate = index === 0 ? -5 : index === 1 ? 0 : 5;
                    rotateY = 0;
                } else {
                    y = "0%";
                    scale = 1;
                    opacity = 1;
                    const t = (cardProgress - 0.6) / 0.4;
                    x = gsap.utils.interpolate(index === 0 ? "100%" : index === 1 ? "0%" : "-100%", "0%", smoothStep(t));
                    rotate = gsap.utils.interpolate(index === 0 ? -5 : index === 1 ? 0 : 5, 0, smoothStep(t));
                    rotateY = smoothStep(t) * 180;
                }

                gsap.set(cardId, { y, x, scale, rotate, opacity });
                gsap.set(innerCard, { rotationY: rotateY });
            });
        }
    });
  `
    document.head.appendChild(script)
})
</script>

<style>
@import '@/assets/eduardbodak.css';
</style>