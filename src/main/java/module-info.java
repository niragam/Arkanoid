module arkanoid {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    exports arkanoid;
    exports arkanoid.animation;
    exports arkanoid.core;
    exports arkanoid.entity;
    exports arkanoid.event;
    exports arkanoid.geometry;
    exports arkanoid.graphics;
    exports arkanoid.graphics.painters;
    exports arkanoid.level;
    exports arkanoid.screen;
    exports arkanoid.util;
}

