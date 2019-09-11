package ru.cleverhause.device.api.dto;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/9/2018.
 */

public class DeviceSetting implements Serializable {
    private Long id;
    private Boolean adj;
    private Boolean rotate;
    private Boolean signaling;

    public DeviceSetting(Long id, Boolean adj, Boolean rotate, Boolean signaling) {
        this.id = id;
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
    }

    public DeviceSetting() {
    }

    public DeviceSetting(DeviceSetting.Builder builder) {
        this.id = builder.id;
        this.adj = builder.adj;
        this.rotate = builder.rotate;
        this.signaling = builder.signaling;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdj() {
        return adj;
    }

    public void setAdj(Boolean adj) {
        this.adj = adj;
    }

    public Boolean getRotate() {
        return rotate;
    }

    public void setRotate(Boolean rotate) {
        this.rotate = rotate;
    }

    public Boolean getSignaling() {
        return signaling;
    }

    public void setSignaling(Boolean signaling) {
        this.signaling = signaling;
    }

    public static class Builder {
        private Long id;
        private Boolean adj;
        private Boolean rotate;
        private Boolean signaling;

        public Builder() {
        }

        public DeviceSetting.Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public DeviceSetting.Builder setAdj(Boolean adj) {
            this.adj = adj;
            return this;
        }

        public DeviceSetting.Builder setRotate(Boolean rotate) {
            this.rotate = rotate;
            return this;
        }

        public DeviceSetting.Builder setSignaling(Boolean signaling) {
            this.signaling = signaling;
            return this;
        }

        public DeviceSetting build() {
            return new DeviceSetting(this);
        }
    }

    @Override
    public String toString() {
        return "DeviceSetting{" +
                "id=" + id +
                ", adj=" + adj +
                ", rotate=" + rotate +
                ", signaling=" + signaling +
                '}';
    }
}