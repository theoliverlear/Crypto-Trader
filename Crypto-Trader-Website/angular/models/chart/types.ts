import {HistoryPoint} from "../currency/types";

export type SparkPoint = {
    date: Date | string | number;
    value: number;
};
export type Margin = {
    top: number;
    right: number;
    bottom: number;
    left: number;
}

export type ChartDisplayProperties = {
    data: SparkPoint[] | HistoryPoint[];
    width: number;
    height: number;
    stroke: string;
    margin: Margin;
    strokeWidth: number;
    textColor: string;
}